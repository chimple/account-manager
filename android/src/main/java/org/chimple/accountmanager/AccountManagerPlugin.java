package org.chimple.accountmanager;

import android.Manifest;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;


@CapacitorPlugin(name = "AccountManager",
        permissions = {
                @Permission(
                        alias = "ACCOUNT_MANAGER",
                        strings = {Manifest.permission.ACCOUNT_MANAGER}
                ),
                @Permission(
                        alias = "GET_ACCOUNTS",
                        strings = {Manifest.permission.GET_ACCOUNTS}
                )
        }
)
public class AccountManagerPlugin extends Plugin {

    public static final String AUTH_TOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String ACCOUNT_TYPE = "com.google";
    private static final String STATE_DIALOG = "state_dialog";
    private static final String STATE_INVALIDATE = "state_invalidate";
//    private final Context CONTEXT = getContext();

    private String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    ;
    private AlertDialog mAlertDialog;
    private boolean mInvalidate;


    @Override
    public void load() {
        System.out.println("Entered load() in android/java layer");
        mAccountManager = AccountManager.get(getContext());
    }

    @PluginMethod()
    public Account getAccount(PluginCall call) {
        Log.d("VSO", "Entered getAccount()" + mAccountManager);
        //Requesting GET_ACCOUNTS permission
        if (getPermissionState("GET_ACCOUNTS") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered getAccount() requesting permission");
            requestPermissionForAlias("GET_ACCOUNTS", call, "accountManagerPermissionCallback");
            System.out.println("getAccount() GET_ACCOUNTS permission granted ");
        } else {
            System.out.println("getAccount() GET_ACCOUNTS permission already granted ");
        }
        Account[] accounts = mAccountManager.getAccounts(); //.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }

        System.out.println("getAccount() result" + account);
        return account;
    }

    /**
     * Add new account to the account manager
     * <p>
     * //     * @param authTokenType
     */

    @PluginMethod()
    public void addNewAccount(PluginCall call) {
//        public void addNewAccount(PluginCall call, String accountType, String authTokenType) {
        Log.d(TAG, "Entered addNewAccount() ");
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(ACCOUNT_TYPE, "auth_token_type", null, null, getActivity(), new AccountManagerCallback<Bundle>() {
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

    /**
     * Get the auth token for an existing account on the AccountManager
     * <p>
     *
     * @param account
     * @param authTokenType
     */
    @PluginMethod()
    public void getExistingAccountAuthToken(PluginCall call, Account account, String authTokenType) {
        Log.d("VSO", "Entered getExistingAccountAuthToken()");
        //Requesting ACCOUNT_MANAGER permission
        if (getPermissionState("ACCOUNT_MANAGER") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered getExistingAccountAuthToken() requesting permission");
            requestPermissionForAlias("ACCOUNT_MANAGER", call, "accountManagerPermissionCallback");
            System.out.println("getExistingAccountAuthToken() ACCOUNT_MANAGER permission granted ");
        } else {
            System.out.println("getExistingAccountAuthToken() location permission already granted ");
        }

        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, getActivity(), null, null);

        new Thread(() -> {
            try {
                Bundle bnd = future.getResult();

                final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                showMessage((authToken != null) ? "SUCCESS!\ntoken: " + authToken : "FAIL");
                Log.d("VSO", "GetToken Bundle is " + bnd);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
        }).start();
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
     * Show all the accounts registered on the account manager. Request an auth token upon user select.
     * <p>
     * //     * @param authTokenType
     */
    @PluginMethod()
    public void showAccountPicker(PluginCall call) {
//    public void showAccountPicker(PluginCall call, final String authTokenType, final boolean invalidate) {

        //Requesting GET_ACCOUNTS permission
        if (getPermissionState("GET_ACCOUNTS") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered showAccountPicker() requesting permission");
            requestPermissionForAlias("GET_ACCOUNTS", call, "accountManagerPermissionCallback");
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
            mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle("Pick Account").setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, name), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    if (invalidate)
                    if (false)
                        invalidateAuthToken(availableAccounts[which], "auth_token_type", call);
                    else
//                        getExistingAccountAuthToken(call);
                        getExistingAccountAuthToken(call, availableAccounts[which], "auth_token_type");
                }
            }).create();
            mAlertDialog.show();
        }
    }

    /**
     * Invalidates the auth token for the account
     *
     * @param account
     * @param authTokenType
     */

    @PluginMethod()
    public void invalidateAuthToken(final Account account, String authTokenType, PluginCall call) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, authTokenType, null, null, null, null);

        new Thread(() -> {
            try {
                Bundle bnd = future.getResult();

                final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                mAccountManager.invalidateAuthToken(account.type, authToken);
                showMessage(account.name + " invalidated");
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
        }).start();
    }

    /**
     * Get an auth token for the account.
     * If not exist - add it and then return its auth token.
     * If one exist - return its auth token.
     * If more than one exists - show a picker and return the select account's auth token.
     *
     * @param accountType
     * @param authTokenType
     */
    @PluginMethod()
    public void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType, PluginCall call) {
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, null, null, null,
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
                }
                , null);
    }

    public void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        Log.e("Something went wrong", msg);
    }

}
