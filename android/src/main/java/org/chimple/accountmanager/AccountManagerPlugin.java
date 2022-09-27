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
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;

import java.io.IOException;


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
        Log.d("VSO", "Entered load() in android/java layer");
        mAccountManager = AccountManager.get(getActivity());
    }

    @PluginMethod()
    public void accountPicker(PluginCall call) {
        try {
            Intent intent = mAccountManager.newChooseAccountIntent(null, null,
                    null, false, null, null, null, null);
            saveCall(call);
            Log.d(TAG, "getAccount: intent" + intent + "  " + intent.getDataString());
//          startActivityForResult(call, intent, "getAccountIntentResult");
            startActivityForResult(call, intent, "authenticateAccountIntentResult");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
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
        Log.d(TAG, "getAccountIntentResult: Bundle extras " + extras.getString("authAccount") + "   " + extras.getString("accountType"));
//
//        Log.d(TAG, "intent.getStringExtra(\"name\")" + result.getData().getStringExtra("id"));
//        Log.d(TAG, "intent.getStringExtra(\"name\")" + result.getData().getStringExtra("name"));
    }

    @ActivityCallback
    private void authenticateAccountIntentResult(PluginCall call, ActivityResult result) {
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        Intent resultData = result.getData();
        Bundle extras = resultData.getExtras();
        Log.d(TAG, "authenticateAccountIntentResult: " + extras.getString("accountType") + "  " + extras.getString("authAccount"));
        String accountType = extras.getString("accountType");
        if (accountType.equals("com.google")) {
            authenticator(call, extras.getString("authAccount"), accountType);
        } else {
            Toast.makeText(getActivity(), "please choose Google Account to Authenticate", Toast.LENGTH_SHORT).show();
            call.reject("false");
        }

    }

    @PluginMethod()
    public Account getAccount(PluginCall call) {
        Log.d("VSO", "Entered getAccount()" + mAccountManager);
        //Requesting GET_ACCOUNTS permission
        if (getPermissionState("GET_ACCOUNTS") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered getAccount() requesting permission");
            requestPermissionForAlias("GET_ACCOUNTS", call, "getAccountPermissionCallback");
            System.out.println("getAccount() GET_ACCOUNTS permission granted ");
        } else {
            System.out.println("getAccount() GET_ACCOUNTS permission already granted ");
        }
        Account[] accounts = mAccountManager.getAccounts(); //.getAccountsByType("com.google");
//        System.out.println("mAccountManager.getAccountsByType(\"com.google\") " + mAccountManager.getAccountsByType("com.google"));
//        Account[] accounts = mAccountManager.getAccountsByType("com.google");
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

    @PluginMethod()
    public void authenticator(PluginCall call, String userName, String AccountType) {
        PluginCall savedCall = getSavedCall();

        if (savedCall == null) {
            return;
        }

        Log.d(TAG, "authenticator: Method called");
        try {
            final AccountManagerFuture<Bundle> future = mAccountManager.confirmCredentials(new Account(userName, AccountType), null, getActivity(), new AccountManagerCallback<Bundle>() {
                @Override
                public void run(AccountManagerFuture<Bundle> future) {
                    try {
                        Bundle bnd = future.getResult();
                        Log.d("VSO", "Authenticator Bundle is " + future.isDone());
                        if (bnd.getBoolean("booleanResult")) {
                            Log.d("VSO", "Account Authentication Success");
                            Toast.makeText(getActivity(), "Account Authentication Success", Toast.LENGTH_SHORT).show();
                            JSObject ret = new JSObject();
                            ret.put("result", true);
                            call.success(ret);
                        } else {
                            Toast.makeText(getActivity(), "Account Authentication Failed", Toast.LENGTH_SHORT).show();
//                            JSObject ret = new JSObject();
//                            ret.put("result", false);
//                            call.success(ret);
                            call.reject("false");
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
     * //     * @param account
     * //     * @param authTokenType
     */
    @PluginMethod()
    public void getExistingAccountAuthToken(PluginCall call) {
        Log.d("VSO", "Entered getExistingAccountAuthToken()");
        //Requesting ACCOUNT_MANAGER permission
        if (getPermissionState("ACCOUNT_MANAGER") != PermissionState.GRANTED) {
            Log.d("VSO", "Entered getExistingAccountAuthToken() requesting permission");
            requestPermissionForAlias("ACCOUNT_MANAGER", call, "accountManagerPermissionCallback");
            System.out.println("getExistingAccountAuthToken() ACCOUNT_MANAGER permission granted ");
        } else {
            System.out.println("getExistingAccountAuthToken() location permission already granted ");
        }

        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(new Account("skandakumar97@gmail.com", "com.google"), ACCOUNT_TYPE, null, getActivity(), null, null);

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
            mAlertDialog = new AlertDialog.Builder(getActivity()).setTitle("Pick Account").setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, name), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    if (invalidate)
                    if (false)
                        invalidateAuthToken(availableAccounts[which], "auth_token_type", call);
                    else
                        getExistingAccountAuthToken(call);
//                        getExistingAccountAuthToken(call, availableAccounts[which], "auth_token_type");
                }
            }).create();
            mAlertDialog.show();
        }
    }

//    public void pickUserAccount() {
//        /*This will list all available accounts on device without any filtering*/
//        Intent intent = mAccountManager.newChooseAccountIntent(null, null,
//                null, false, null, null, null, null);
//        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
//    }
//    /*After manually selecting every app related account, I got its Account type using the code below*/
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
//            // Receiving a result from the AccountPicker
//            if (resultCode == RESULT_OK) {
//                System.out.println(data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
//                System.out.println(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(this, R.string.pick_account, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

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
