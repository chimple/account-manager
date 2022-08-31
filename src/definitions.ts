export interface AccountManagerPlugin {
  getAccount(): Promise<{}>;
  addNewAccount(options: { accountType: string, authTokenType: string }): Promise<void>;
  getExistingAccountAuthToken(options: { account: any, authTokenType: string }): Promise<void>;
  showAccountPicker(options: { authTokenType: string, invalidate: boolean }): Promise<void>;
  getTokenForAccountCreateIfNeeded(options: { accountType: string, authTokenType: string }): Promise<void>;
  invalidateAuthToken(options: { account: any, authTokenType: string }): Promise<void>;
}