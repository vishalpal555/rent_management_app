package com.vishalpal555.rentmanagement.service;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.vishalpal555.rentmanagement.entity.RentManagement;
import com.vishalpal555.rentmanagement.global.Constants;
import com.vishalpal555.rentmanagement.global.MockData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FirestoreCloudDbService {
    private static final String TAG = FirestoreCloudDbService.class.getName();
    FirebaseFirestore firestore;
    CollectionReference firestoreCollection;
    DocumentReference firestoreDocument;
    private static final String RENTMANAGEMENT_ADMINS_FIELD_STRING = "admins";

    public  FirestoreCloudDbService(){
        firestore = FirebaseFirestore.getInstance();
        firestoreCollection = firestore.collection(Constants.RENT_MANAGEMENT_FIREBASE_REF);
    }

    public void createRentManagementWithId(Activity activity, String userForeignKey){
        firestoreDocument = firestoreCollection.document(userForeignKey);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                Log.i(TAG, "createRentManagementWithId: " +userForeignKey +" exists");
            } else {
                firestoreDocument.set(new RentManagement(userForeignKey))
                        .addOnCompleteListener(setTask -> {
                            if (setTask.isSuccessful()) {
                                Log.i(TAG, "createRentManagementWithId: firestoreDocument.set document created for " + userForeignKey);
                                Toast.makeText(activity, "Database created", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "createRentManagementDb: firestoreDocument.set", e);
                        });
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "createRentManagementDb: firestoreDocument.get() ", e);
        });
    }

    public void appendAdmins(Activity activity, String rentManagementId, Set<String> newAdminSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()) {
                List<String> prevAdmins = (ArrayList<String>) getTask.getResult().getData().get(RENTMANAGEMENT_ADMINS_FIELD_STRING);
                newAdminSet.addAll(prevAdmins);
                firestoreDocument.update(RENTMANAGEMENT_ADMINS_FIELD_STRING, new ArrayList<>(newAdminSet))
                        .addOnCompleteListener(updateTask -> {
                            if(updateTask.isSuccessful()){
                                Log.i(TAG, "appendAdmins: firestoreDocument.update added admins " + newAdminSet);
                                Toast.makeText(activity, "Admins added", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "appendAdmins: firestoreDocument.update", e);
                        });
            } else {
                Log.e(TAG, "appendAdmins: document doesn't exists");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "appendAdmins: firestoreDocument.get() ", e);
        });
    }
}
