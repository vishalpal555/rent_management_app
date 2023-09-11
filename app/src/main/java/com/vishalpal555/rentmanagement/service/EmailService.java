package com.vishalpal555.rentmanagement.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.vishalpal555.rentmanagement.global.Constants;
import com.vishalpal555.rentmanagement.global.RespCodes;
/** @noinspection Since15*/
public class EmailService {
    UpdateUser updateUser;
    Validator validator;
    private static final String TAG = EmailService.class.getName();
    public void verifyUser(Activity activity, FirebaseUser firebaseUser, String displayName, String phoneNumber, Intent successNextIntent, Intent failureNextIntent){
        firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
                Log.d(TAG, "invoked verifyUser ---> onCompletion: ");
                if (task.isSuccessful()) {
                    Log.d(TAG, "verifyUser: email sent successful");
                    if(!displayName.isBlank()){
                        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build());
                    }
//                    if(!phoneNumber.isBlank() && phoneNumber.length() == 10){
//
//                    }
                    if(successNextIntent != null){
                        activity.runOnUiThread(() -> {
                            activity.startActivity(successNextIntent);
                            activity.finish();
                        });
                    }
                } else {
                    Log.e(TAG, "verifyUser: email sending failed");
                    firebaseUser.delete();
                    Toast.makeText(activity, "Please try again", Toast.LENGTH_LONG).show();
                }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "verifyUser --> onFailure: ", e.getCause());
            firebaseUser.delete();
        });
    }

    public void resetPassword(Activity activity, FirebaseAuth mAuth, String email, Intent successNextIntent, Intent failureNextIntent){
        if(validator.isEmail(email)) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "sendPasswordResetEmail error: ", e);
                        if(e instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(activity, "provided username doesn't exist", Toast.LENGTH_LONG).show();
                        }
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "sendPasswordResetEmail successful");
                            Toast.makeText(activity, "Password reset email sent", Toast.LENGTH_LONG).show();
                        }
                        Log.e(TAG, "sendPasswordResetEmail failed");
                    });
        } else {
            Toast.makeText(activity, "Put a correct email address", Toast.LENGTH_LONG).show();
        }
    }
}
