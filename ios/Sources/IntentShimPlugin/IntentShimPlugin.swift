import Foundation
import Capacitor

/**
 * IntentShim iOS bridge — no-op.
 *
 * com-darryncampbell-cordova-plugin-intent is Android-only. This bridge exists
 * solely so dual-stack builds that include IntentShim do not crash on iOS.
 * Every method resolves cleanly. No events are ever emitted.
 *
 * Capacitor 8 pattern: method list is declared via `CAPBridgedPlugin`, so no
 * separate ObjC target / `Plugin.m` / `CAP_PLUGIN` macro is needed.
 */
@objc(IntentShimPlugin)
public class IntentShimPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "IntentShimPlugin"
    public let jsName = "IntentShim"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "sendBroadcast", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "registerBroadcastReceiver", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "unregisterBroadcastReceiver", returnType: CAPPluginReturnPromise)
    ]

    @objc func sendBroadcast(_ call: CAPPluginCall) {
        CAPLog.print("[IntentShim][iOS] sendBroadcast is a no-op on iOS.")
        call.resolve()
    }

    @objc func registerBroadcastReceiver(_ call: CAPPluginCall) {
        CAPLog.print("[IntentShim][iOS] registerBroadcastReceiver is a no-op on iOS; 'broadcastReceived' will never fire.")
        call.resolve()
    }

    @objc func unregisterBroadcastReceiver(_ call: CAPPluginCall) {
        CAPLog.print("[IntentShim][iOS] unregisterBroadcastReceiver is a no-op on iOS.")
        call.resolve()
    }
}
