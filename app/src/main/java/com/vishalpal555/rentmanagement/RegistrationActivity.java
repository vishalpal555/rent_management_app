package com.vishalpal555.rentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.vishalpal555.rentmanagement.global.Constants;
import com.vishalpal555.rentmanagement.service.EmailService;
import com.vishalpal555.rentmanagement.service.UpdateUser;
import com.vishalpal555.rentmanagement.service.Validator;

import java.util.Objects;

/** @noinspection Since15*/
public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = RegistrationActivity.class.getName();
    EmailService emailService;
    private Button openManRegBtn;
    private Button registerSubmitBtn;
    private ConstraintLayout manualRegLayout;
    private TextView loginLink;
    private EditText fullnameInput;
    private EditText usernameInput;
//    private EditText phoneNumberInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private FirebaseAuth mAuth;
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        manualRegLayout = findViewById(R.id.manualRegLayout);
        openManRegBtn = findViewById(R.id.openRegisterManuallyButton);

        mAuth = FirebaseAuth.getInstance();

        final Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        openManRegBtn.setOnClickListener(view -> {
            if(manualRegLayout.getVisibility() == View.GONE && openManRegBtn.getText().toString().equals(Constants.REGISTER_MANUALLY)){
                manualRegLayout.setVisibility(View.VISIBLE);
                manualRegLayout.startAnimation(slideIn);
                openManRegBtn.setText(Constants.HIDE_MANUAL_REG_STRING);
            } else if (manualRegLayout.getVisibility() == View.VISIBLE && openManRegBtn.getText().toString().equals(Constants.HIDE_MANUAL_REG_STRING)) {
                manualRegLayout.setVisibility(View.GONE);
                openManRegBtn.setText(Constants.REGISTER_MANUALLY);
            } else {
                Log.d(TAG, "onCreate: manualRegLayout button issue");
            }
        });

        Intent loginIntent = new Intent(this, LoginActivity.class);
        loginLink = findViewById(R.id.loginLinkText);
        loginLink.setOnClickListener(view -> {
            startActivity(loginIntent);
        });

        registerSubmitBtn = findViewById(R.id.registerSubmit);
        fullnameInput = findViewById(R.id.registerFullname);
        usernameInput = findViewById(R.id.registerUsername);
//        phoneNumberInput = findViewById(R.id.registerPhoneNumber);
        passwordInput = findViewById(R.id.registerPassword);
        passwordConfirmInput = findViewById(R.id.registerConfirmPassword);

        if(Constants.isMock){
            fullnameInput.setText("Dummy One");
            usernameInput.setText("vishalpaldeveloper+dummy1@gmail.com");
            passwordInput.setText("12412343");
            passwordConfirmInput.setText("12412343");
        }

        registerSubmitBtn.setOnClickListener(view -> {
            validator = new Validator();
            if(fullnameInput.getText().toString().isBlank() || usernameInput.getText().toString().isBlank() ||
            passwordInput.getText().toString().isBlank() || passwordConfirmInput.getText().toString().isBlank()){
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else if(!validator.isEmail(usernameInput.getText().toString())){
                Toast.makeText(this, "Type Email correctly", Toast.LENGTH_SHORT).show();
            } else if (!passwordInput.getText().toString().equals(passwordConfirmInput.getText().toString())) {
                Toast.makeText(this, "Password confirmation and password must be same", Toast.LENGTH_SHORT).show();
//            } else if (phoneNumberInput.getText().toString().length() != 10 && !phoneNumberInput.getText().toString().isBlank()) {
//                Toast.makeText(this, "Phone number has to be only 10 digit or empty", Toast.LENGTH_LONG).show();
            } else {
                mAuth.createUserWithEmailAndPassword(usernameInput.getText().toString(), passwordInput.getText().toString())
                        .addOnCompleteListener(task -> {
                            Log.d(TAG, "createUserWithEmailAndPassword --> onCompletion");
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.d(TAG, "createUserWithEmailAndPassword: success" + Objects.requireNonNull(user));
                                emailService = new EmailService();
                                Intent mainActivityIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Toast.makeText(this, "Sending email verification", Toast.LENGTH_LONG).show();
                                emailService.verifyUser(RegistrationActivity.this,
                                                            user,
                                                            fullnameInput.getText().toString(),
                                                            null,
                                                            mainActivityIntent,
                                               null);

                            } else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Log.d(TAG, "createUserWithEmailAndPassword: failed : ", task.getException());
                                Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d(TAG, "createUserWithEmailAndPassword: failed" + task.getException());
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "createUserWithEmailAndPassword: failureListener : ", e.getCause());
                        });
            }
        });
    }
}