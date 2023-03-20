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
* [`invalidateAuthToken()`](#invalidateauthtoken)
* [`authenticator(...)`](#authenticator)
* [`accountPicker()`](#accountpicker)
* [Interfaces](#interfaces)

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
getExistingAccountAuthToken(options: { userName: string; authTokenType: string; }) => Promise<void>
```

| Param         | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`options`** | <code>{ userName: string; authTokenType: string; }</code> |

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


### invalidateAuthToken()

```typescript
invalidateAuthToken() => Promise<void>
```

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
accountPicker() => Promise<authData>
```

**Returns:** <code>Promise&lt;<a href="#authdata">authData</a>&gt;</code>

--------------------


### Interfaces


#### authData

| Prop              | Type                |
| ----------------- | ------------------- |
| **`authAccount`** | <code>string</code> |
| **`sourcedId`**   | <code>string</code> |
| **`endpointUrl`** | <code>string</code> |
| **`addedType`**   | <code>string</code> |
| **`authToken`**   | <code>string</code> |

</docgen-api>
