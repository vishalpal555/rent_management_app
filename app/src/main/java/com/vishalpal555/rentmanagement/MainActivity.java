package com.vishalpal555.rentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vishalpal555.rentmanagement.global.Constants;
import com.vishalpal555.rentmanagement.service.LogoutService;
import com.vishalpal555.rentmanagement.service.RentManagementFirestoreService;
import com.vishalpal555.rentmanagement.service.RoomsFirestoreService;
import com.vishalpal555.rentmanagement.service.Validator;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private RentManagementFirestoreService rentManagementFirestoreService;
    private RoomsFirestoreService roomsFirestoreService;
    private String fire;
    private EditText connectDatabaseInp;
    private Button createDbBtn;
    private Button connectDbBtn;
    private SharedPreferences sharedPreferences;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logoutMenu) {
            LogoutService.USER_SIGN_OUT(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
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
                Log.d(TAG, "onCreate: checking if user verified.....");
                firebaseUser.reload().addOnCompleteListener(firebaseUserReloadTask -> {
                    if (firebaseUserReloadTask.isSuccessful()) {
                        Log.i(TAG, "user reload successful: ");
                        if (firebaseUser.isEmailVerified()) {
                            Log.i(TAG, "user email verification done at: " +LocalDateTime.now());
                            Log.i(TAG, "userReloadSchedule shutting down...... ");
                            userReloadSchedule.shutdownNow();
                            Log.i(TAG, "UI recreating..... ");
                            runOnUiThread(this::recreate);
                        }
                    }
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "user reload failed : ", e);
                });
            }, delay, period, TimeUnit.SECONDS);
        }

        createDbBtn = findViewById(R.id.createDatabaseBtn);
        connectDatabaseInp = findViewById(R.id.connectDatabaseInp);
        connectDbBtn = findViewById(R.id.connectDatabaseBtn);

        mainActivityTextTrial = findViewById(R.id.mainActivityTextTrial);
        mainActivityMenu = findViewById(R.id.mainActivityMenu);

        errorMsgMainActivity = findViewById(R.id.errorMsgMainActivity);
        if(firebaseUser != null){
            mainActivityTextTrial.setText(firebaseUser.getEmail());
            openGmailBtn = findViewById(R.id.openGmailBtn);

            if(firebaseUser.isEmailVerified()){
                errorMsgMainActivity.setVisibility(View.GONE);
                openGmailBtn.setVisibility(View.GONE);

                sharedPreferences = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                if(sharedPreferences.contains(Constants.SHARED_PREFERENCE_RENTMANAGEMENT_DOCUMENT_KEY)){
                    rentManagementFirestoreService = new RentManagementFirestoreService();
                    rentManagementFirestoreService.connectRentManagementWithId(this, Objects.requireNonNull(sharedPreferences.getAll().get(Constants.SHARED_PREFERENCE_RENTMANAGEMENT_DOCUMENT_KEY)).toString());
                }

                rentManagementFirestoreService = new RentManagementFirestoreService();
                createDbBtn.setOnClickListener(view -> {
                    rentManagementFirestoreService.createRentManagementWithId(this);
                });
                connectDbBtn.setOnClickListener(view -> {
                    rentManagementFirestoreService.connectRentManagementWithId(this, connectDatabaseInp.getText().toString());
                });

            } else {
                createDbBtn.setVisibility(View.GONE);
                connectDatabaseInp.setVisibility(View.GONE);
                connectDbBtn.setVisibility(View.GONE);
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
//                    mAuth.signOut();
//                    Log.i(TAG, "popupMenu.setOnMenuItemClickListener: logged out");
//                    Intent loginIntent = new Intent(this, LoginActivity.class);
//                    startActivity(loginIntent);
//                    return true;
                }
                Log.i(TAG, "popupMenu.setOnMenuItemClickListener: selected none");
                return false;
            });
            popupMenu.show();
        });
    }
}