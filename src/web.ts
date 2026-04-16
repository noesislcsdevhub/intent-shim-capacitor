import { WebPlugin } from '@capacitor/core';

import type {
  IntentShimPlugin,
  RegisterBroadcastReceiverOptions,
  SendBroadcastOptions,
} from './definitions';

/**
 * Web no-op implementation. IntentShim wraps Android-specific APIs; there is
 * no meaningful web behavior. All methods resolve cleanly and log a warning
 * so misconfigured dual-stack builds surface visibly during development.
 */
export class IntentShimWeb extends WebPlugin implements IntentShimPlugin {
  async sendBroadcast(_options: SendBroadcastOptions): Promise<void> {
    console.warn('[IntentShim][Web] sendBroadcast is not supported on web.');
  }

  async registerBroadcastReceiver(
    _options: RegisterBroadcastReceiverOptions,
  ): Promise<void> {
    console.warn('[IntentShim][Web] registerBroadcastReceiver is not supported on web.');
  }

  async unregisterBroadcastReceiver(): Promise<void> {
    console.warn('[IntentShim][Web] unregisterBroadcastReceiver is not supported on web.');
  }
}
