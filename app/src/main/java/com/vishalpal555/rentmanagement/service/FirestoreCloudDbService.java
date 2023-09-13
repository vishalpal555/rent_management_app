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

import java.util.Objects;

public class FirestoreCloudDbService {
    private static final String TAG = FirestoreCloudDbService.class.getName();
    FirebaseFirestore firestore;
    CollectionReference firestoreCollection;
    DocumentReference firestoreDocument;

    public  FirestoreCloudDbService(){
        firestore = FirebaseFirestore.getInstance();
        firestoreCollection = firestore.collection(Constants.RENT_MANAGEMENT_FIREBASE_REF);
    }

    public void createRentManagementWithId(Activity activity, String userForeignKey){
        firestoreDocument = firestoreCollection.document(userForeignKey);
        firestoreDocument.addSnapshotListener((value, error) -> {
            if(!Objects.requireNonNull(value).exists()){
                firestoreDocument.set(new RentManagement(userForeignKey))
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Log.i(TAG, "createRentManagementWithId: document created for " +userForeignKey);
                                Toast.makeText(activity, "Database created", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "createRentManagementDb: ", e);
                        });
            } else{
                Log.d(TAG, "createRentManagementWithId: document exists");
            }
            if(error != null) Log.e(TAG, "createRentManagementWithId: ", error);
        });
    }

}
