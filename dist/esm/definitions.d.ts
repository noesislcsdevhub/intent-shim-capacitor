import type { PluginListenerHandle } from '@capacitor/core';
/**
 * IntentShim — Capacitor port of com-darryncampbell-cordova-plugin-intent.
 *
 * Scope: the three methods used by the OutSystems Zebra DataWedge library —
 * sendBroadcast, registerBroadcastReceiver, unregisterBroadcastReceiver.
 * Android-only behavior; iOS methods are no-ops that resolve successfully.
 *
 * Cordova observable behavior is the source of truth. Where Cordova asymmetries
 * exist, they are preserved deliberately (see method-level notes).
 */
export interface IntentShimPlugin {
    /**
     * Sends an Android broadcast intent with the given action and optional extras.
     *
     * Cordova parity: all `extras` values are coerced to String before being
     * attached to the Intent (via `String.valueOf`). Consumers that need to pass
     * structured data should stringify it themselves (e.g. `JSON.stringify(obj)`)
     * — this matches how the OutSystems DataWedge connector already behaves.
     *
     * iOS: no-op.
     */
    sendBroadcast(options: SendBroadcastOptions): Promise<void>;
    /**
     * Registers a dynamic BroadcastReceiver for the given filter actions
     * (and optionally categories). Any receiver previously registered by this
     * plugin is unregistered first, mirroring the Cordova implementation.
     *
     * Received intents are delivered via the `'broadcastReceived'` event —
     * subscribe using `addListener('broadcastReceived', cb)`.
     *
     * Android 13+ (API 33+): the receiver is registered with
     * `Context.RECEIVER_EXPORTED`, required on API 34+ for broadcasts originating
     * from other apps (e.g. the Zebra DataWedge service).
     *
     * iOS: no-op. No events will ever fire on iOS.
     */
    registerBroadcastReceiver(options: RegisterBroadcastReceiverOptions): Promise<void>;
    /**
     * Unregisters the currently registered BroadcastReceiver, if any.
     * Safe to call when no receiver is registered (swallowed internally).
     *
     * iOS: no-op.
     */
    unregisterBroadcastReceiver(): Promise<void>;
    /**
     * Subscribes to broadcast events from the currently registered receiver.
     * Emits a {@link BroadcastIntent} payload matching the Cordova
     * `getIntentJson` shape.
     */
    addListener(eventName: 'broadcastReceived', listenerFunc: (intent: BroadcastIntent) => void): Promise<PluginListenerHandle>;
    /**
     * Removes all listeners registered via {@link addListener}.
     */
    removeAllListeners(): Promise<void>;
}
export interface SendBroadcastOptions {
    /** Intent action string, e.g. "com.symbol.datawedge.api.ACTION". */
    action: string;
    /**
     * Flat key/value map of extras. Values are coerced to String to match
     * the Cordova implementation — structured payloads must be pre-stringified.
     */
    extras?: Record<string, string | number | boolean>;
}
export interface RegisterBroadcastReceiverOptions {
    /** Intent filter actions. At least one action is required. */
    filterActions: string[];
    /** Optional intent filter categories. */
    filterCategories?: string[];
}
/**
 * Shape of a received broadcast intent. Mirrors Cordova's `getIntentJson`
 * output so dual-stack consumer code does not need platform-specific parsing.
 */
export interface BroadcastIntent {
    action: string | null;
    type: string | null;
    extras: Record<string, unknown> | null;
    categories: string[] | null;
    flags: number;
    component: string | null;
    data: string | null;
    package: string | null;
    /** Present only when the intent carries ClipData (KitKat+). */
    clipItems?: Array<Record<string, unknown>>;
}
