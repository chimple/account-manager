import { WebPlugin } from '@capacitor/core';
import type { AccountManagerPlugin } from './definitions';
export declare class AccountManagerWeb extends WebPlugin implements AccountManagerPlugin {
    accountPicker(): Promise<{}>;
    getAccount(): Promise<{}>;
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
    addNewAccount(options: {
        accountType: string;
        authTokenType: string;
    }): Promise<void>;
    authenticator(options: {
        userName: string;
        AccountType: string;
    }): Promise<void>;
}
