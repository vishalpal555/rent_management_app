package com.vishalpal555.rentmanagement.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.vishalpal555.rentmanagement.LoginActivity;
import com.vishalpal555.rentmanagement.RentManagementActivity;
import com.vishalpal555.rentmanagement.global.Constants;

public class LogoutService {
    private static final String TAG = LogoutService.class.getName();

    public static void USER_SIGN_OUT(Activity activity){
        activity.deleteSharedPreferences(Constants.SHARED_PREFERENCE_FILE_KEY);
        FirebaseAuth.getInstance().signOut();
        Log.i(TAG, "logged out");
        Intent loginActivity = new Intent(activity, LoginActivity.class);
        activity.startActivity(loginActivity);
    }
}
