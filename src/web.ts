import { WebPlugin } from '@capacitor/core';

import type { AccountManagerPlugin } from './definitions';

export class AccountManagerWeb extends WebPlugin implements AccountManagerPlugin {
  async getAccount(): Promise<{}> {
    throw new Error('Method not implemented.');
  }
  async getExistingAccountAuthToken(options: { account: any; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
  async showAccountPicker(options: { authTokenType: string; invalidate: boolean; }): Promise<void> {
    throw new Error('Method not implemented.'+ options);
  }
  async getTokenForAccountCreateIfNeeded(options: { accountType: string; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.'+ options);
  }
  async invalidateAuthToken(options: { account: any; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.'+ options);
  }
  async addNewAccount(options: { accountType: string; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.'+ options);
  }
}
