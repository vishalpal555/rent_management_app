package com.vishalpal555.rentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.vishalpal555.rentmanagement.global.Constants;
import com.vishalpal555.rentmanagement.service.EmailService;
import com.vishalpal555.rentmanagement.service.Validator;

import java.util.Objects;
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();
    private TextView registerLink;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private TextView passwordResetLink;
    private FirebaseAuth mAuth;
    EmailService emailService = new EmailService();
    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        }

        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        registerLink = findViewById(R.id.registerLinkText);
        registerLink.setOnClickListener(view -> {
            startActivity(registrationIntent);
        });

        usernameInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.registerSubmit);

        if(Constants.isMock){
            usernameInput.setText("vishalpaldeveloper+dummy1@gmail.com");
            passwordInput.setText("123456");
        }

        loginBtn.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            validator = new Validator();
            if(!username.isEmpty() && !password.isEmpty()) {
                if(username.isBlank() || password.isBlank()){
                    Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                } else if (validator.isEmail(username)) {
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Log.d(TAG, "signInWithEmail : success " + Objects.requireNonNull(user).getEmail());
                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                } else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                    Log.d(TAG, "signInWithEmail: failure ", task.getException());
                                    Toast.makeText(LoginActivity.this, "wrong password or username", Toast.LENGTH_LONG).show();
                                }else {
                                    Log.d(TAG, "signInWithEmail: failure ", task.getException());
                                    Toast.makeText(LoginActivity.this, "login fail", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "signInWithEmail: failureListener ", e.getCause());
                            });
                } else {
                    Toast.makeText(this, "type email correctly", Toast.LENGTH_LONG).show();
                }
            }
        });

        passwordResetLink = findViewById(R.id.passwordResetLinkText);
        passwordResetLink.setOnClickListener(view -> {
            if(usernameInput.getText().toString().isBlank()){
                Toast.makeText(this, "Put email address or username", Toast.LENGTH_LONG).show();
            }else {
                emailService.resetPassword(this, mAuth, usernameInput.getText().toString(), null, null);
            }
        });
    }
}