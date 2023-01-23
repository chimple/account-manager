package org.chimple.accountmanager;

import android.Manifest;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;

import java.io.IOException;

@CapacitorPlugin(name = "AccountManager", permissions = {
        @Permission(alias = "ACCOUNT_MANAGER", strings = {Manifest.permission.ACCOUNT_MANAGER}),
        @Permission(alias = "GET_ACCOUNTS", strings = {Manifest.permission.GET_ACCOUNTS})
})

public class AccountManagerPlugin extends Plugin {

    public static final String AUTH_TOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String ACCOUNT_TYPE = "com.google";
    private static final String STATE_DIALOG = "state_dialog";
    private static final String STATE_INVALIDATE = "state_invalidate";
    // private final Context CONTEXT = getContext();

    private String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    ;
    private AlertDialog mAlertDialog;
    private boolean mInvalidate;
//    private OAuthCallbackListener authCallbackListener;

    @Override
    public void load() {
        Log.d("VSO", "Entered load() in android/java layer");
        mAccountManager = AccountManager.get(getActivity());
    }

    @PluginMethod()
    public void accountPicker(PluginCall call) {

        // try {
        // Intent intent = mAccountManager.newChooseAccountIntent(null, null,
        // null, false, null, null, null, null);
        // saveCall(call);
        // Log.d(TAG, "getAccount: intent" + intent + " " + intent.getDataString());
        // // startActivityForResult(call, intent, "getAccountIntentResult");
        // startActivityForResult(call, intent, "authenticateAccountIntentResult");

        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return;

        String input = "";

        try {
            Log.d(TAG, "Enterd accountPicker: ");
            Intent authIntent = new Intent("com.ustadmobile.AUTH_GET_TOKEN", Uri.parse("local-auth://" + input + "?:"));
            Log.d(TAG, "Enterd accountPicker: " + authIntent);
            startActivityForResult(call, authIntent, "getUstadIntentResult");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ActivityCallback
    private void getUstadIntentResult(PluginCall call, ActivityResult result) {
        if (call == null) {
            return;
        }

        // Do something with the result data
        Log.d(TAG, "getUstadIntentResult: " + result + "   " + result.getData());

        Intent resultData = result.getData();
        Log.d(TAG, "getUstadIntentResult: result.getResultCode() " + result.getResultCode());

        Bundle extras = resultData.getExtras();
        Log.d(TAG, "getUstadIntentResult: Bundle extras " + extras.getString(AccountManager.KEY_ACCOUNT_NAME) + "   "
                + AccountManager.KEY_ACCOUNT_TYPE + "   " + extras.getString(AccountManager.KEY_AUTHTOKEN));

        int resultCode = result.getResultCode();
        String addedName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);
        String addedType = extras.getString(AccountManager.KEY_ACCOUNT_TYPE);
        String authToken = extras.getString(AccountManager.KEY_AUTHTOKEN);

        if (addedName != null && addedType != null && authToken != null) {
            Log.d("VSO", "getToken Success ");
            Toast.makeText(getActivity(), "GetToken Success " + authToken,
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "run: result" + "   " + resultCode + "   " + addedName + "   " + addedType + "   " + authToken);
            JSObject ret = new JSObject();
            ret.put("result", resultCode + "," + addedName + "," + addedType + "," + authToken);
            call.success(ret);
        } else {
            Toast.makeText(getActivity(), "getToken Failed",
                    Toast.LENGTH_SHORT).show();
            call.reject("false");
        }

//        return GetTokenResult(resultCode, addedName, addedType, authToken);

    }

    @ActivityCallback
    private void getAccountIntentResult(PluginCall call, ActivityResult result) {
        if (call == null) {
            return;
        }

        // Do something with the result data
        Log.d(TAG, "getAccountIntentResult: " + result + "   " + result.getData());

        Intent resultData = result.getData();
        Log.d(TAG, "getAccountIntentResult: resultData " + resultData.getExtras().toString());

        Bundle extras = resultData.getExtras();
        Log.d(TAG, "getAccountIntentResult: Bundle extras " + extras.getString("authAccount") + "   "
                + extras.getString("accountType"));
        //
        // Log.d(TAG, "intent.getStringExtra(\"name\")" +
        // result.getData().getStringExtra("id"));
        // Log.d(TAG, "intent.getStringExtra(\"name\")" +
        // result.getData().getStringExtra("name"));
    }

    @ActivityCallback
    private void authenticateAccountIntentResult(PluginCall call, ActivityResult result) {
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        Intent resultData = result.getData();
        Bundle extras = resultData.getExtras();
        Log.d(TAG, "authenticateAccountIntentResult: " + resultData + "  "
                + resultData.getStringExtra(AccountManager.KEY_AUTHTOKEN) + "  " + extras.toString());
        String accountType = extras.getString("accountType");
        if (accountType.equals("com.google")) {
            Account account = new Account(extras.getString("authAccount"), accountType);
            Log.d("authToken account", account.toString());

            authenticator(call, account);

        } else {
            Toast.makeText(getActivity(), "please choose Google Account to Authenticate", Toast.LENGTH_SHORT)
                    .show();
            call.reject("false");
        }

    }

    @PluginMethod()
    public Account getAccount(PluginCall call) {
        Log.d("VSO", "Entered getAccount()" + mAccountManager);
        // Requesting GET_ACCOUNTS permission
        if (getPermissionState("GET_ACCOUNTS") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered getAccount() requesting permission");
            requestPermissionForAlias("GET_ACCOUNTS", call, "getAccountPermissionCallback");
            System.out.println("getAccount() GET_ACCOUNTS permission granted ");
        } else {
            System.out.println("getAccount() GET_ACCOUNTS permission already granted ");
        }
        Account[] accounts = mAccountManager.getAccounts(); // .getAccountsByType("com.google");
        // System.out.println("mAccountManager.getAccountsByType(\"com.google\") " +
        // mAccountManager.getAccountsByType("com.google"));
        // Account[] accounts = mAccountManager.getAccountsByType("com.google");
        System.out.println("accounts " + accounts.length + "   " + accounts);
        Account account;

        for (int i = 0; i < accounts.length; i++) {
            System.out.println("account " + accounts[i].name + "   " + accounts[i].type);
        }

        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        System.out.println("getAccount() account" + account);
        return account;
    }

    @PermissionCallback
    public void getAccountPermissionCallback(PluginCall call) {
        if (getPermissionState("GET_ACCOUNTS") == PermissionState.GRANTED) {
            System.out.println("accountManagerPermissionCallback() Called GET_ACCOUNTS permission granted");
            return;
        } else {
            call.reject("accountManagerPermissionCallback Permission is rejected");
        }
    }

    /**
     * Add new account to the account manager
     * <p>
     * // * @param authTokenType
     */

    @PluginMethod()
    public void addNewAccount(PluginCall call) {
        // public void addNewAccount(PluginCall call, String accountType, String
        // authTokenType) {
        Log.d(TAG, "Entered addNewAccount() ");
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(ACCOUNT_TYPE, "auth_token_type",
                null,
                null, getActivity(), new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bnd = future.getResult();
                            showMessage("Account was created");
                            Log.d("VSO", "AddNewAccount Bundle is " + bnd);

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                }, null);
    }

    @PluginMethod()
    public void authenticator(PluginCall call, Account account) {
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        Log.d(TAG, "authenticator: Method called");
        try {
            final AccountManagerFuture<Bundle> future = mAccountManager.confirmCredentials(account, null,
                    getActivity(),
                    new AccountManagerCallback<Bundle>() {
                        @Override
                        public void run(AccountManagerFuture<Bundle> future) {
                            try {
                                synchronized (this) {
                                    Bundle result = invalidateAuthToken(account, "ah");
                                    Log.d(TAG, "run: before wait" + result);
                                    wait(2000);
                                    Log.d(TAG, "run: after wait" + result);
                                    final String authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
                                    showMessage((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL");
                                    Bundle bnd = future.getResult();
                                    Log.d("VSO", "Authenticator future is " + future);
                                    Log.d("VSO", "Authenticator Bundle is " + bnd
                                            + bnd.getBoolean(AccountManager.KEY_BOOLEAN_RESULT));
                                    if (bnd.getBoolean(AccountManager.KEY_BOOLEAN_RESULT) && authToken != null) {
                                        Log.d("VSO", "Account Authentication Success ");
                                        Toast.makeText(getActivity(), "Account Authentication Success",
                                                Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "run: result" + result);
                                        JSObject ret = new JSObject();
                                        ret.put("result", result);
                                        call.success(ret);
                                    } else {
                                        Toast.makeText(getActivity(), "Account Authentication Failed",
                                                Toast.LENGTH_SHORT).show();
                                        // JSObject ret = new JSObject();
                                        // ret.put("result", false);
                                        // call.success(ret);
                                        call.reject("false");
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                showMessage(e.getMessage());
                            }
                        }
                    }, null);
            Log.d(TAG, "authenticator: run method completed ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the auth token for an existing account on the AccountManager
     * <p>
     * <p>
     * // * @param account
     * // * @param authTokenType
     */
    @PluginMethod()
    public void getExistingAccountAuthToken(PluginCall call, String userName, String accountType) {
        Log.d("VSO", "Entered getExistingAccountAuthToken()");
        // Requesting ACCOUNT_MANAGER permission
        if (getPermissionState("ACCOUNT_MANAGER") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered getExistingAccountAuthToken() requesting permission");
            requestPermissionForAlias("ACCOUNT_MANAGER", call, "accountManagerPermissionCallback");
            System.out.println("getExistingAccountAuthToken() ACCOUNT_MANAGER permission granted ");
        } else {
            System.out.println("getExistingAccountAuthToken() location permission already granted ");
        }
        try {
            Account account = new Account(userName, accountType);
            String authToken = mAccountManager.peekAuthToken(account, AccountManager.KEY_AUTH_TOKEN_LABEL);
            // getAuthToken(call, account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(new
        // Account(userName, accountType), ACCOUNT_TYPE, null, null, null, null);
        //
        // new Thread(() -> {
        // try {
        // Bundle bnd = future.getResult();
        //
        // final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
        // showMessage((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL");
        // Log.d("VSO", "GetToken Bundle is " + bnd);
        // } catch (Exception e) {
        // e.printStackTrace();
        // showMessage(e.getMessage());
        // }
        // }).start();
    }

    @PermissionCallback
    public void accountManagerPermissionCallback(PluginCall call) {
        if (getPermissionState("GET_ACCOUNTS") == PermissionState.GRANTED) {
            System.out.println("accountManagerPermissionCallback() Called GET_ACCOUNTS permission granted");
            return;
        } else {
            call.reject("accountManagerPermissionCallback Permission is rejected");
        }
    }

    /**
     * Show all the accounts registered on the account manager. Request an auth
     * token upon user select.
     * <p>
     * // * @param authTokenType
     */
    @PluginMethod()
    public void showAccountPicker(PluginCall call) {
        // public void showAccountPicker(PluginCall call, final String authTokenType,
        // final boolean invalidate) {

        // Requesting GET_ACCOUNTS permission
        if (getPermissionState("GET_ACCOUNTS") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered getAccount() requesting permission");
            requestPermissionForAlias("GET_ACCOUNTS", call, "getAccountPermissionCallback");
            System.out.println("getAccount() GET_ACCOUNTS permission granted ");
        } else {
            System.out.println("getAccount() GET_ACCOUNTS permission already granted ");
        }

        mInvalidate = false;
        final Account availableAccounts[] = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
        Log.d(TAG, "Entered showAccountPicker: " + availableAccounts);

        if (availableAccounts.length == 0) {
            System.out.println("if (availableAccounts.length == 0) {");
            Toast.makeText(getActivity(), "No accounts", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("else");
            String name[] = new String[availableAccounts.length];
            for (int i = 0; i < availableAccounts.length; i++) {
                System.out.println("availableAccounts[i].name " + i + "    " + availableAccounts[i].name);
                name[i] = availableAccounts[i].name;
            }

            // Account picker
            mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle("Pick Account")
                    .setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, name),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // if (invalidate)
                                    // if (false)
                                    // invalidateAuthToken(availableAccounts[which], "auth_token_type", call);
                                    // else
                                    // getExistingAccountAuthToken(call);
                                    // getExistingAccountAuthToken(call, availableAccounts[which],
                                    // "auth_token_type");
                                }
                            })
                    .create();
            mAlertDialog.show();
        }
    }

    // public void pickUserAccount() {
    // /*This will list all available accounts on device without any filtering*/
    // Intent intent = mAccountManager.newChooseAccountIntent(null, null,
    // null, false, null, null, null, null);
    // startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    // }
    // /*After manually selecting every app related account, I got its Account type
    // using the code below*/
    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent data)
    // {
    // if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
    // // Receiving a result from the AccountPicker
    // if (resultCode == RESULT_OK) {
    // System.out.println(data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
    // System.out.println(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
    // } else if (resultCode == RESULT_CANCELED) {
    // Toast.makeText(this, R.string.pick_account, Toast.LENGTH_LONG).show();
    // }
    // }
    // }

    // @PluginMethod()
    // public Bundle getAuthToken(PluginCall call, Account account) throws
    // NetworkErrorException {
    // Bundle authBundle = new Bundle();
    // String authToken = mAccountManager.peekAuthToken(account,
    // AUTH_TOKEN_TYPE_FULL_ACCESS);
    // Log.d(TAG, "getAuthToken: authToken " + authToken);
    // if (!TextUtils.isEmpty(authToken)) {
    // authBundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
    // authBundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
    // authBundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
    // Log.d(TAG, "getAuthToken: authToken is not empty " + authBundle);
    // } else {
    // Log.d(TAG, "getAuthToken: result is null");
    //// Intent authIntent = new Intent();
    //// authIntent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
    // response);
    //// authBundle.putParcelable(AccountManager.KEY_INTENT, authIntent);
    // }
    // return authBundle;
    // }

    /**
     * Invalidates the auth token for the account
     * <p>
     * // * @param account
     * // * @param authTokenType
     *
     * @return
     */

    public Bundle invalidateAuthToken(final Account account, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null,
                getActivity(), null, null);
        final Bundle[] bnd = new Bundle[1];
        Thread thread = new Thread(() -> {
            try {
                bnd[0] = future.getResult();
                Log.d(TAG, "invalidateAuthToken: bnd " + bnd[0]);
                // return future.getResult();
                // authToken = bnd.get().getString(AccountManager.KEY_AUTHTOKEN);
                // showMessage((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL");
                // mAccountManager.invalidateAuthToken(account.type, authToken);
                // showMessage(account.name + " invalidated");
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
        });

        synchronized (this) {
            thread.start();
            try {
                wait(2000);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
            Log.d(TAG, "invalidateAuthToken: bnd before return " + bnd[0]);
            return bnd[0];
        }
    }

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    private Activity act;

    public void getAuthToken(Account account, final OAuthCallbackListener authCallbackListener) {
        mAccountManager.getAuthToken(account, SCOPE, null, act,
                new AccountManagerCallback<Bundle>() {
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
                            authCallbackListener.callback(token);
                        } catch (OperationCanceledException e) {
                            authCallbackListener.callback(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);

    }

    public static interface OAuthCallbackListener {
        public void callback(String authToken);
    }

    /**
     * Get an auth token for the account.
     * If not exist - add it and then return its auth token.
     * If one exist - return its auth token.
     * If more than one exists - show a picker and return the select account's auth
     * token.
     *
     * @param accountType
     * @param authTokenType
     */
    @PluginMethod()
    public void getTokenForAccountCreateIfNeeded(PluginCall call, String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType,
                authTokenType,
                null, getActivity(), null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {
                            bnd = future.getResult();
                            final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            showMessage(((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL"));
                            Log.d("VSO", "GetTokenForAccount Bundle is " + bnd);

                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }
                    }
                }, null);
    }

    public void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        Log.e("Something went wrong", msg);
    }

}
