package com.vishalpal555.rentmanagement.service;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vishalpal555.rentmanagement.entity.Room;
import com.vishalpal555.rentmanagement.global.MockData;

public class RoomsFirestoreService {
    private static final String TAG = RoomsFirestoreService.class.getName();
    private FirebaseFirestore firestore;
    private CollectionReference firestoreCollection;
    private DocumentReference firestoreDocument;
    private static final String ROOMS_FIREBASE_REF = "rooms";
    public RoomsFirestoreService(){
        firestore = FirebaseFirestore.getInstance();
        firestoreCollection = firestore.collection(ROOMS_FIREBASE_REF);
    }

    public void createRoomWithId(Activity activity, Room roomData){
        firestoreDocument = firestoreCollection.document(roomData.getRoomId());
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                Log.e(TAG, "createRoomWithId: " +roomData.getRoomId() +" already exists");
            } else {
                firestoreDocument.set(roomData)
                        .addOnCompleteListener(addTask -> {
                            if(addTask.isSuccessful()){
                                Log.i(TAG, "createRoom: firestoreCollection.document().set() added Room with id " +roomData.getRoomId());
                            }
                        });
            }
        });
    }
    public void removeRoomWithId(Activity activity, String roomId){
        firestoreDocument = firestoreCollection.document(roomId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                firestoreDocument.delete().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.i(TAG, "removeRoomWithId: " +roomId +" deleted");
                    } else {
                        Log.e(TAG, "removeRoomWithId: " +roomId +" deletion failed");
                    }
                });
            } else {
                Log.e(TAG, "removeRoom: rentManagement document doesn't exists with id " +roomId);
            }
        });
    }
}
