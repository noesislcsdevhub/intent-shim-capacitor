package com.noesis.intentshim;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * Core Android implementation. Wraps three Android SDK operations:
 *   - Context.sendBroadcast
 *   - Context.registerReceiver / BroadcastReceiver
 *   - Context.unregisterReceiver
 *
 * Behavior is ported 1:1 from com-darryncampbell-cordova-plugin-intent for the
 * three in-scope methods. The emitted broadcast payload mirrors the Cordova
 * {@code getIntentJson} shape so consumer code is platform-agnostic across
 * the dual-stack wrapper.
 *
 * Cordova asymmetries are preserved deliberately (see inline notes).
 */
public class IntentShim {

    public static final String TAG = "IntentShim";

    /** Callback sink for broadcast events. The plugin bridge forwards to {@code notifyListeners}. */
    public interface BroadcastListener {
        void onBroadcast(JSObject intentJson);
    }

    private final Context context;
    private final BroadcastListener listener;
    private BroadcastReceiver currentReceiver = null;

    public IntentShim(Context context, BroadcastListener listener) {
        // Use application context to outlive the host activity for the receiver lifecycle.
        this.context = context.getApplicationContext();
        this.listener = listener;
    }

    // --------------------------------------------------------------------
    // sendBroadcast
    // --------------------------------------------------------------------

    public void sendBroadcast(String action, JSObject extras) throws JSONException {
        Intent intent = new Intent();
        intent.setAction(action);
        applyExtras(intent, extras);
        context.sendBroadcast(intent);
        Log.d(TAG, "sendBroadcast action=" + action);
    }

    /**
     * Cordova parity: every extra is stored as a String. Nested structures must
     * be pre-stringified by the caller (DataWedge profile JSON blobs already are).
     */
    private void applyExtras(Intent intent, JSObject extras) throws JSONException {
        if (extras == null) return;
        Iterator<String> keys = extras.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = extras.get(key);
            if (value == null || JSONObject.NULL.equals(value)) {
                intent.putExtra(key, (String) null);
            } else {
                intent.putExtra(key, String.valueOf(value));
            }
        }
    }

    // --------------------------------------------------------------------
    // registerBroadcastReceiver
    // --------------------------------------------------------------------

    public void registerBroadcastReceiver(JSArray filterActions, JSArray filterCategories) throws JSONException {
        // Cordova parity: unregister any previously registered receiver first.
        unregisterBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        for (int i = 0; i < filterActions.length(); i++) {
            filter.addAction(filterActions.getString(i));
        }
        if (filterCategories != null) {
            for (int i = 0; i < filterCategories.length(); i++) {
                filter.addCategory(filterCategories.getString(i));
            }
        }

        currentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (listener == null) return;
                try {
                    listener.onBroadcast(intentToJson(intent));
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to serialize received intent: " + e.getMessage(), e);
                }
            }
        };

        // API 33+ (Android 13) accepts the export flag; API 34+ (Android 14) requires
        // it for any broadcast whose source is another app. DataWedge broadcasts
        // originate from the DataWedge service process → RECEIVER_EXPORTED.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(currentReceiver, filter, Context.RECEIVER_EXPORTED);
        } else {
            context.registerReceiver(currentReceiver, filter);
        }
        Log.d(TAG, "registerBroadcastReceiver actions=" + filterActions.length()
            + " categories=" + (filterCategories != null ? filterCategories.length() : 0));
    }

    // --------------------------------------------------------------------
    // unregisterBroadcastReceiver
    // --------------------------------------------------------------------

    public void unregisterBroadcastReceiver() {
        if (currentReceiver == null) return;
        try {
            context.unregisterReceiver(currentReceiver);
        } catch (IllegalArgumentException e) {
            // Cordova parity: swallow — receiver may have been unregistered externally.
        }
        currentReceiver = null;
    }

    // --------------------------------------------------------------------
    // Intent → JSObject  (Cordova getIntentJson parity)
    // --------------------------------------------------------------------

    private JSObject intentToJson(Intent intent) throws JSONException {
        JSObject out = new JSObject();

        // ClipData items — only present on KitKat+ and only when attached.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                out.put("clipItems", clipDataToJsonArray(clipData));
            }
        }

        out.put("type", intent.getType());
        out.put("extras", bundleToJson(intent.getExtras()));
        out.put("action", intent.getAction());
        out.put("categories", intent.getCategories() != null
            ? new JSONArray(intent.getCategories())
            : JSONObject.NULL);
        out.put("flags", intent.getFlags());
        out.put("component", intent.getComponent() != null
            ? intent.getComponent().flattenToString()
            : JSONObject.NULL);
        out.put("data", intent.getData() != null
            ? intent.getData().toString()
            : JSONObject.NULL);
        out.put("package", intent.getPackage());
        return out;
    }

    private JSONArray clipDataToJsonArray(ClipData clipData) throws JSONException {
        JSONArray arr = new JSONArray();
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        for (int i = 0; i < clipData.getItemCount(); i++) {
            ClipData.Item item = clipData.getItemAt(i);
            JSONObject itemJson = new JSONObject();
            itemJson.put("htmlText", item.getHtmlText());
            itemJson.put("intent", item.getIntent() != null ? item.getIntent().toString() : JSONObject.NULL);
            itemJson.put("text", item.getText() != null ? item.getText().toString() : JSONObject.NULL);
            if (item.getUri() != null) {
                String uri = item.getUri().toString();
                String type = cr.getType(item.getUri());
                itemJson.put("uri", uri);
                itemJson.put("type", type != null ? type : JSONObject.NULL);
                itemJson.put("extension", type != null ? mime.getExtensionFromMimeType(type) : JSONObject.NULL);
            } else {
                itemJson.put("uri", JSONObject.NULL);
            }
            arr.put(itemJson);
        }
        return arr;
    }

    private static Object bundleToJson(Bundle bundle) throws JSONException {
        if (bundle == null) return JSONObject.NULL;
        JSONObject out = new JSONObject();
        for (String key : bundle.keySet()) {
            out.put(key, toJsonValue(bundle.get(key)));
        }
        return out;
    }

    /**
     * Recursively converts a Bundle value into a JSON-serializable value.
     * Arrays → JSONArray, nested Bundles → JSONObject, primitives pass through,
     * anything else → String.valueOf.
     */
    private static Object toJsonValue(Object value) throws JSONException {
        if (value == null) {
            return JSONObject.NULL;
        } else if (value instanceof Bundle) {
            return bundleToJson((Bundle) value);
        } else if (value.getClass().isArray()) {
            JSONArray result = new JSONArray();
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                result.put(i, toJsonValue(Array.get(value, i)));
            }
            return result;
        } else if (value instanceof String
                || value instanceof Boolean
                || value instanceof Integer
                || value instanceof Long
                || value instanceof Double
                || value instanceof Float) {
            return value;
        } else {
            return String.valueOf(value);
        }
    }
}
