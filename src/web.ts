import { WebPlugin } from '@capacitor/core';

import type { AccountManagerPlugin, authData } from './definitions';

export class AccountManagerWeb
  extends WebPlugin
  implements AccountManagerPlugin
{
  accountPicker(): Promise<authData> {
    throw new Error('Method not implemented.');
  }
  async getAccount(): Promise<{}> {
    throw new Error('Method not implemented.');
  }
  async getExistingAccountAuthToken(options: {
    userName: string;
    authTokenType: string;
  }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
  async showAccountPicker(): Promise<void> {
    throw new Error('Method not implemented.');
  }
  async getTokenForAccountCreateIfNeeded(options: {
    accountType: string;
    authTokenType: string;
  }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
  async invalidateAuthToken(): Promise<void> {
    throw new Error('Method not implemented.');
  }
  async addNewAccount(options: {
    accountType: string;
    authTokenType: string;
  }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }

  async authenticator(options: {
    userName: string;
    AccountType: string;
  }): Promise<void> {
    throw new Error('Method not implemented.' + options);
  }
}
