import { WebPlugin } from '@capacitor/core';
/**
 * Web no-op implementation. IntentShim wraps Android-specific APIs; there is
 * no meaningful web behavior. All methods resolve cleanly and log a warning
 * so misconfigured dual-stack builds surface visibly during development.
 */
export class IntentShimWeb extends WebPlugin {
    async sendBroadcast(_options) {
        console.warn('[IntentShim][Web] sendBroadcast is not supported on web.');
    }
    async registerBroadcastReceiver(_options) {
        console.warn('[IntentShim][Web] registerBroadcastReceiver is not supported on web.');
    }
    async unregisterBroadcastReceiver() {
        console.warn('[IntentShim][Web] unregisterBroadcastReceiver is not supported on web.');
    }
}
//# sourceMappingURL=web.js.map