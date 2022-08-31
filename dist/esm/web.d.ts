import { WebPlugin } from '@capacitor/core';
import type { AccountManagerPlugin } from './definitions';
export declare class AccountManagerWeb extends WebPlugin implements AccountManagerPlugin {
    getAccount(): Promise<{}>;
    getExistingAccountAuthToken(options: {
        account: any;
        authTokenType: string;
    }): Promise<void>;
    showAccountPicker(options: {
        authTokenType: string;
        invalidate: boolean;
    }): Promise<void>;
    getTokenForAccountCreateIfNeeded(options: {
        accountType: string;
        authTokenType: string;
    }): Promise<void>;
    invalidateAuthToken(options: {
        account: any;
        authTokenType: string;
    }): Promise<void>;
    addNewAccount(options: {
        accountType: string;
        authTokenType: string;
    }): Promise<void>;
}
