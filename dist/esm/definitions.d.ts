export interface AccountManagerPlugin {
    getAccount(): Promise<{}>;
    addNewAccount(options: {
        accountType: string;
        authTokenType: string;
    }): Promise<void>;
    getExistingAccountAuthToken(options: {
        userName: string;
        authTokenType: string;
    }): Promise<void>;
    showAccountPicker(): Promise<void>;
    getTokenForAccountCreateIfNeeded(options: {
        accountType: string;
        authTokenType: string;
    }): Promise<void>;
    invalidateAuthToken(): Promise<void>;
    authenticator(options: {
        userName: string;
        AccountType: string;
    }): Promise<void>;
    accountPicker(): Promise<{}>;
}
