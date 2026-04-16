'use strict';

var core = require('@capacitor/core');

const IntentShim = core.registerPlugin('IntentShim', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.IntentShimWeb()),
});

/**
 * Web no-op implementation. IntentShim wraps Android-specific APIs; there is
 * no meaningful web behavior. All methods resolve cleanly and log a warning
 * so misconfigured dual-stack builds surface visibly during development.
 */
class IntentShimWeb extends core.WebPlugin {
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

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    IntentShimWeb: IntentShimWeb
});

exports.IntentShim = IntentShim;
//# sourceMappingURL=plugin.cjs.js.map
