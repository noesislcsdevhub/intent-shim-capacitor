import { registerPlugin } from '@capacitor/core';
const IntentShim = registerPlugin('IntentShim', {
    web: () => import('./web').then((m) => new m.IntentShimWeb()),
});
export * from './definitions';
export { IntentShim };
//# sourceMappingURL=index.js.map