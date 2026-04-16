package com.noesis.intentshim;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONException;

/**
 * Capacitor bridge for IntentShim. Thin routing layer over {@link IntentShim}.
 *
 * The plugin name is "IntentShim" (not "IntentShimPlugin") for parity with the
 * Cordova feature name, so the OutSystems dual-stack wrapper can reach it via
 * {@code window.Capacitor.Plugins.IntentShim}.
 */
@CapacitorPlugin(name = "IntentShim")
public class IntentShimPlugin extends Plugin {

    public static final String EVENT_BROADCAST = "broadcastReceived";

    private IntentShim implementation;

    @Override
    public void load() {
        implementation = new IntentShim(
            getContext(),
            intentJson -> notifyListeners(EVENT_BROADCAST, intentJson)
        );
    }

    @PluginMethod
    public void sendBroadcast(PluginCall call) {
        String action = call.getString("action");
        if (action == null || action.isEmpty()) {
            call.reject("action is required");
            return;
        }
        // extras is optional; null means "no extras"
        JSObject extras = call.getObject("extras");
        try {
            implementation.sendBroadcast(action, extras);
            call.resolve();
        } catch (JSONException e) {
            call.reject("Failed to parse extras: " + e.getMessage(), e);
        }
    }

    @PluginMethod
    public void registerBroadcastReceiver(PluginCall call) {
        JSArray filterActions = call.getArray("filterActions");
        if (filterActions == null || filterActions.length() == 0) {
            call.reject("filterActions is required and must be a non-empty array");
            return;
        }
        // filterCategories is optional
        JSArray filterCategories = call.getArray("filterCategories");
        try {
            implementation.registerBroadcastReceiver(filterActions, filterCategories);
            call.resolve();
        } catch (JSONException e) {
            call.reject("Failed to parse filters: " + e.getMessage(), e);
        }
    }

    @PluginMethod
    public void unregisterBroadcastReceiver(PluginCall call) {
        implementation.unregisterBroadcastReceiver();
        call.resolve();
    }

    @Override
    protected void handleOnDestroy() {
        // Prevent leaked receivers when the host activity is destroyed.
        if (implementation != null) {
            implementation.unregisterBroadcastReceiver();
        }
        super.handleOnDestroy();
    }
}
