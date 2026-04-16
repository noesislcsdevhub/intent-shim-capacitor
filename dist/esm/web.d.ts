import { WebPlugin } from '@capacitor/core';
import type { IntentShimPlugin, RegisterBroadcastReceiverOptions, SendBroadcastOptions } from './definitions';
/**
 * Web no-op implementation. IntentShim wraps Android-specific APIs; there is
 * no meaningful web behavior. All methods resolve cleanly and log a warning
 * so misconfigured dual-stack builds surface visibly during development.
 */
export declare class IntentShimWeb extends WebPlugin implements IntentShimPlugin {
    sendBroadcast(_options: SendBroadcastOptions): Promise<void>;
    registerBroadcastReceiver(_options: RegisterBroadcastReceiverOptions): Promise<void>;
    unregisterBroadcastReceiver(): Promise<void>;
}
