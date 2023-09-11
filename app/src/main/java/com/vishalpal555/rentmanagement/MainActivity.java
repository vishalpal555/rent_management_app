package com.vishalpal555.rentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vishalpal555.rentmanagement.global.Constants;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private ImageButton mainActivityMenu;
    private TextView mainActivityTextTrial;
    private TextView errorMsgMainActivity;
    private Button openGmailBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        ScheduledExecutorService userReloadSchedule = Executors.newSingleThreadScheduledExecutor();
        int delay = 0;
        int period = 3;
        if(!firebaseUser.isEmailVerified()){
            userReloadSchedule.scheduleWithFixedDelay(() -> {
                firebaseUser.reload().addOnCompleteListener(firebaseUserReloadTask -> {
                    if (firebaseUserReloadTask.isSuccessful()) {
                        Log.i(TAG, "user reload successful: ");
                        if (firebaseUser.isEmailVerified()) {
                            Log.i(TAG, "user email verification done: ");
                            Log.i(TAG, "userReloadSchedule shutting down...... ");
                            userReloadSchedule.shutdownNow();
                            Log.i(TAG, "UI recreating..... ");
                            recreate();
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "user reload failed : ", e);
                });
            }, delay, period, TimeUnit.SECONDS);
        }

        mainActivityTextTrial = findViewById(R.id.mainActivityTextTrial);
        mainActivityMenu = findViewById(R.id.mainActivityMenu);

        errorMsgMainActivity = findViewById(R.id.errorMsgMainActivity);
        if(firebaseUser != null){
            mainActivityTextTrial.setText(firebaseUser.getEmail());
            openGmailBtn = findViewById(R.id.openGmailBtn);

            if(firebaseUser.isEmailVerified()){
                errorMsgMainActivity.setVisibility(View.GONE);
                openGmailBtn.setVisibility(View.GONE);
            } else {
                errorMsgMainActivity.setText(getString(R.string.verify_user_alert_msg));
                errorMsgMainActivity.setVisibility(View.VISIBLE);
                errorMsgMainActivity.bringToFront();
                openGmailBtn.setVisibility(View.VISIBLE);
                openGmailBtn.bringToFront();
                openGmailBtn.setOnClickListener(view -> {
                    Intent openGmailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GMAIL_LINK));
                    startActivity(openGmailIntent);
                });
            }
        }
        mainActivityMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.main_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.logoutMenu) {
                    mAuth.signOut();
                    Log.i(TAG, "popupMenu.setOnMenuItemClickListener: logged out");
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivity(loginIntent);
                    return true;
                }
                Log.i(TAG, "popupMenu.setOnMenuItemClickListener: selected none");
                return false;
            });
            popupMenu.show();
        });


    }
}