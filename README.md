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
* [`showAccountPicker()`](#showaccountpicker)
* [`getTokenForAccountCreateIfNeeded(...)`](#gettokenforaccountcreateifneeded)
* [`invalidateAuthToken(...)`](#invalidateauthtoken)
* [`authenticator(...)`](#authenticator)
* [`accountPicker()`](#accountpicker)

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


### showAccountPicker()

```typescript
showAccountPicker() => Promise<void>
```

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


### authenticator(...)

```typescript
authenticator(options: { userName: string; AccountType: string; }) => Promise<void>
```

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code>{ userName: string; AccountType: string; }</code> |

--------------------


### accountPicker()

```typescript
accountPicker() => Promise<{}>
```

**Returns:** <code>Promise&lt;{}&gt;</code>

--------------------

</docgen-api>
