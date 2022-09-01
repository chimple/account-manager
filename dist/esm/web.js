import { WebPlugin } from '@capacitor/core';
export class AccountManagerWeb extends WebPlugin {
    async getAccount() {
        throw new Error('Method not implemented.');
    }
    async getExistingAccountAuthToken(options) {
        throw new Error('Method not implemented.' + options);
    }
    async showAccountPicker(options) {
        throw new Error('Method not implemented.' + options);
    }
    async getTokenForAccountCreateIfNeeded(options) {
        throw new Error('Method not implemented.' + options);
    }
    async invalidateAuthToken(options) {
        throw new Error('Method not implemented.' + options);
    }
    async addNewAccount(options) {
        throw new Error('Method not implemented.' + options);
    }
}
//# sourceMappingURL=web.js.map