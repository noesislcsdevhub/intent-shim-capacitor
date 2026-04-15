import { WebPlugin } from '@capacitor/core';

import type { IntentShimPlugin } from './definitions';

export class IntentShimWeb extends WebPlugin implements IntentShimPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
