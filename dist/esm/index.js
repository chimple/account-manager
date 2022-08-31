import { registerPlugin } from '@capacitor/core';
const AccountManager = registerPlugin('AccountManager', {
    web: () => import('./web').then(m => new m.AccountManagerWeb()),
});
export * from './definitions';
export { AccountManager };
//# sourceMappingURL=index.js.map