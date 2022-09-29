import { WebPlugin } from '@capacitor/core';

import type { AccountManagerPlugin } from './definitions';

export class AccountManagerWeb extends WebPlugin implements AccountManagerPlugin {
  accountPicker(): Promise<{ result: any }> {
    return new Promise<any>((resolve) => {
      resolve({ "result": true });
    });
  }
  async getAccount(): Promise<{}> {
    throw new Error('Method not implemented.');
  }
  async getExistingAccountAuthToken(options: { account: any; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
  async showAccountPicker(): Promise<void> {
    throw new Error('Method not implemented.');
  }
  async getTokenForAccountCreateIfNeeded(options: { accountType: string; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
  async invalidateAuthToken(options: { account: any; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
  async addNewAccount(options: { accountType: string; authTokenType: string; }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }

  async authenticator(options: { userName: string; AccountType: string; }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
}
