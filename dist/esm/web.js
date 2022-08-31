import { WebPlugin } from '@capacitor/core';
export class AccountManagerWeb extends WebPlugin {
    async getAccount() {
        throw new Error('Method not implemented.');
    }
    async getExistingAccountAuthToken(options) {
        // throw new Error('Method not implemented.');
        console.log('getExistingAccountAuthToken', options);
    }
    async showAccountPicker(options) {
        // throw new Error('Method not implemented.');
        console.log('showAccountPicker', options);
    }
    async getTokenForAccountCreateIfNeeded(options) {
        // throw new Error('Method not implemented.');
        console.log('getTokenForAccountCreateIfNeeded', options);
    }
    async invalidateAuthToken(options) {
        // throw new Error('Method not implemented.');
        console.log('invalidateAuthToken', options);
    }
    async addNewAccount(options) {
        console.log('addNewAccount', options);
        // return options;
    }
}
//# sourceMappingURL=web.js.map