package com.vishalpal555.rentmanagement.service;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vishalpal555.rentmanagement.entity.RentManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RentManagementFirestoreService {
    private static final String TAG = RentManagementFirestoreService.class.getName();
    FirebaseFirestore firestore;
    CollectionReference firestoreCollection;
    DocumentReference firestoreDocument;
    private static final String RENT_MANAGEMENT_FIREBASE_REF = "rent_management";
    private static final String RENTMANAGEMENT_ADMINS_FIELD_STRING = "admins";

    public RentManagementFirestoreService(){
        firestore = FirebaseFirestore.getInstance();
        firestoreCollection = firestore.collection(RENT_MANAGEMENT_FIREBASE_REF);
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
                                Log.i(TAG, "createRentManagementWithId: firestoreDocument.set() document created for " + userForeignKey);
                                Toast.makeText(activity, "Database created", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "createRentManagementDb: firestoreDocument.set ", e);
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
                if(newAdminSet.addAll(prevAdmins)) {
                    firestoreDocument.update(RENTMANAGEMENT_ADMINS_FIELD_STRING, new ArrayList<>(newAdminSet))
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.i(TAG, "appendAdmins: firestoreDocument.update() added admins " + newAdminSet);
                                    Toast.makeText(activity, "Admins added", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "appendAdmins: firestoreDocument.update() ", e);
                            });
                } else {
                    Toast.makeText(activity, "Admins already present", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "appendAdmins: document doesn't exists ");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "appendAdmins: firestoreDocument.get() ", e);
        });
    }

    public void removeAdmins(Activity activity, String rentManagementId, Set<String> removeAdminSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        removeAdminSet.remove(rentManagementId);    //removing default admin id
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                List<String> prevAdmins = (ArrayList<String>) getTask.getResult().getData().get(RENTMANAGEMENT_ADMINS_FIELD_STRING);
                if(prevAdmins.removeAll(removeAdminSet)) {
                    firestoreDocument.update(RENTMANAGEMENT_ADMINS_FIELD_STRING, prevAdmins)
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.i(TAG, "removeAdmins: firestoreDocument.update() removed admins " + removeAdminSet);
                                    Toast.makeText(activity, "Admins removed", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "removeAdmins: firestoreDocument.update() ", e);
                            });
                } else {
                    Toast.makeText(activity, "Admins not present", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "removeAdmins: document doesn't exists ");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "removeAdmins: firestoreDocument.get() ", e);
        });
    }

}
