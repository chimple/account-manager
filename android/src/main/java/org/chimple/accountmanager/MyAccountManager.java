package org.chimple.accountmanager;

import android.util.Log;

public class MyAccountManager {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    public String openMap(String value) {
        Log.i("AccountManager openMap", value);
        return value;
    }
}
