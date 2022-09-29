var capacitorAccountManager = (function (exports, core) {
    'use strict';

    const AccountManager = core.registerPlugin('AccountManager', {
        web: () => Promise.resolve().then(function () { return web; }).then(m => new m.AccountManagerWeb()),
    });

    class AccountManagerWeb extends core.WebPlugin {
        accountPicker() {
            throw new Error('Method not implemented.');
        }
        async getAccount() {
            throw new Error('Method not implemented.');
        }
        async getExistingAccountAuthToken(options) {
            throw new Error('Method not implemented.' + options);
        }
        async showAccountPicker() {
            throw new Error('Method not implemented.');
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
        async authenticator(options) {
            throw new Error('Method not implemented.' + options);
        }
    }

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        AccountManagerWeb: AccountManagerWeb
    });

    exports.AccountManager = AccountManager;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
