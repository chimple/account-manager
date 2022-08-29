import { WebPlugin } from '@capacitor/core';

import type { AccountManagerPlugin } from './definitions';

export class AccountManagerWeb extends WebPlugin implements AccountManagerPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
