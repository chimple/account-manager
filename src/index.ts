import { registerPlugin } from '@capacitor/core';

import type { AccountManagerPlugin } from './definitions';

const AccountManager = registerPlugin<AccountManagerPlugin>('AccountManager', {
  web: () => import('./web').then(m => new m.AccountManagerWeb()),
});

export * from './definitions';
export { AccountManager };
