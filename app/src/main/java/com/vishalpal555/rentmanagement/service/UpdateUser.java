package com.vishalpal555.rentmanagement.service;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.vishalpal555.rentmanagement.global.Constants;

import java.util.concurrent.TimeUnit;

public class UpdateUser {
    private final static String TAG = UpdateUser.class.getName();
    private int result = Constants.FAIL;
    public int updatePhonenumber(Activity activity, FirebaseUser firebaseUser, String phoneNumber){
        if(phoneNumber.length() != 10){
            result = Constants.CANCELLED;
        } else {
            PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder()
                                                    .setPhoneNumber(phoneNumber)
                                                    .setTimeout(Constants.TIMEOUT_5_MIN, TimeUnit.MINUTES)
                                                    .setActivity(activity)
                                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                        @Override
                                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                                            firebaseUser.updatePhoneNumber(phoneAuthCredential).addOnCompleteListener(task -> {
                                                                Log.d(TAG, "onVerificationCompleted: " +task);
                                                                if(task.isSuccessful()){
                                                                    result = Constants.SUCCESS;
                                                                }
                                                            });
                                                        }
                                                        @Override
                                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                                            Log.e(TAG, "onVerificationFailed: ", e);
                                                            result = Constants.FAIL;
                                                        }
                                                        @Override
                                                        public void onCodeSent(@NonNull String verificationId,
                                                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                                            // The SMS verification code has been sent to the provided phone number, we
                                                            // now need to ask the user to enter the code and then construct a credential
                                                            // by combining the code with a verification ID.
                                                            Log.d(TAG, "onCodeSent:" + verificationId);

                                                            // Save verification ID and resending token so we can use them later
//                                                            mVerificationId = verificationId;
//                                                            mResendToken = token;
                                                        }
                                                    })
                                                    .build();
            PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
        }
        return result;
    }
}
