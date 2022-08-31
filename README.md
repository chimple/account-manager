# account-manager

Capacitor plugin for AccountManager

## Install

```bash
npm install account-manager
npx cap sync
```

## API

<docgen-index>

* [`getAccount()`](#getaccount)
* [`addNewAccount(...)`](#addnewaccount)
* [`getExistingAccountAuthToken(...)`](#getexistingaccountauthtoken)
* [`showAccountPicker(...)`](#showaccountpicker)
* [`getTokenForAccountCreateIfNeeded(...)`](#gettokenforaccountcreateifneeded)
* [`invalidateAuthToken(...)`](#invalidateauthtoken)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getAccount()

```typescript
getAccount() => Promise<{}>
```

**Returns:** <code>Promise&lt;{}&gt;</code>

--------------------


### addNewAccount(...)

```typescript
addNewAccount(options: { accountType: string; authTokenType: string; }) => Promise<void>
```

| Param         | Type                                                         |
| ------------- | ------------------------------------------------------------ |
| **`options`** | <code>{ accountType: string; authTokenType: string; }</code> |

--------------------


### getExistingAccountAuthToken(...)

```typescript
getExistingAccountAuthToken(options: { account: any; authTokenType: string; }) => Promise<void>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code>{ account: any; authTokenType: string; }</code> |

--------------------


### showAccountPicker(...)

```typescript
showAccountPicker(options: { authTokenType: string; invalidate: boolean; }) => Promise<void>
```

| Param         | Type                                                         |
| ------------- | ------------------------------------------------------------ |
| **`options`** | <code>{ authTokenType: string; invalidate: boolean; }</code> |

--------------------


### getTokenForAccountCreateIfNeeded(...)

```typescript
getTokenForAccountCreateIfNeeded(options: { accountType: string; authTokenType: string; }) => Promise<void>
```

| Param         | Type                                                         |
| ------------- | ------------------------------------------------------------ |
| **`options`** | <code>{ accountType: string; authTokenType: string; }</code> |

--------------------


### invalidateAuthToken(...)

```typescript
invalidateAuthToken(options: { account: any; authTokenType: string; }) => Promise<void>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code>{ account: any; authTokenType: string; }</code> |

--------------------

</docgen-api>
