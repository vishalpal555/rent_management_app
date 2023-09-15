package com.vishalpal555.rentmanagement.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vishalpal555.rentmanagement.entity.RentManagement;
import com.vishalpal555.rentmanagement.global.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RentManagementFirestoreService {
    private static final String TAG = RentManagementFirestoreService.class.getName();
    private FirebaseFirestore firestore;
    private CollectionReference firestoreCollection;
    private DocumentReference firestoreDocument;
    private static final String RENT_MANAGEMENT_FIREBASE_REF = "rent_management";
    private static final String RENTMANAGEMENT_ADMINS_FIELD_STRING = "admins";
    private static final String RENTMANAGEMENT_EMPLOYEES_FIELD_STRING = "employees";
    private static final String RENTMANAGEMENT_ROOMS_FIELD_STRING = "roomsList";
    private SharedPreferences sharedPreferences;
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

    public void connectRentManagementWithId(Activity activity, String rentManagementId, String userid){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                List<String> adminList = (ArrayList<String>)getTask.getResult().get(RENTMANAGEMENT_ADMINS_FIELD_STRING);
                sharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
                sharedPreferences.edit().putString(Constants.SHARED_PREFERENCE_RENTMANAGEMENT_DOCUMENT_KEY, rentManagementId).apply();
                Log.d(TAG, "connectRentManagementWithId: connection saved to shared_pref");
                activity.recreate();
            } else {
                Toast.makeText(activity, "Database doesn't exist", Toast.LENGTH_LONG).show();
            }
        });
    }

    //admins service
    public void appendAdmins(Activity activity, String rentManagementId, Set<String> newAdminSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()) {
                List<String> prevAdmins = (ArrayList<String>) getTask.getResult().get(RENTMANAGEMENT_ADMINS_FIELD_STRING);
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
                Log.e(TAG, "appendAdmins: rentManagement document doesn't exists with id " +rentManagementId);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "appendAdmins: firestoreDocument.get() ", e);
        });
    }

    public void removeAdmins(Activity activity, String rentManagementId, Set<String> removeAdminSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        removeAdminSet.remove(rentManagementId);    //removing default admin id from removeAdminSet
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                List<String> prevAdmins = (ArrayList<String>) getTask.getResult().get(RENTMANAGEMENT_ADMINS_FIELD_STRING);
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
                Log.e(TAG, "removeAdmins: rentManagement document doesn't exists with id " +rentManagementId);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "removeAdmins: firestoreDocument.get() ", e);
        });
    }

    //employees service
    public void appendEmployees(Activity activity, String rentManagementId, Set<String> newEmployeeSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()) {
                List<String> prevEmployees = (ArrayList<String>) getTask.getResult().get(RENTMANAGEMENT_EMPLOYEES_FIELD_STRING);
                if(newEmployeeSet.addAll(prevEmployees)) {
                    firestoreDocument.update(RENTMANAGEMENT_EMPLOYEES_FIELD_STRING, new ArrayList<>(newEmployeeSet))
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.i(TAG, "appendEmployees: firestoreDocument.update() added employees" + newEmployeeSet);
                                    Toast.makeText(activity, "Employees added", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "appendEmployees: firestoreDocument.update() ", e);
                            });
                } else {
                    Toast.makeText(activity, "Employees already present", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "appendEmployees: rentManagement document doesn't exists with id " +rentManagementId);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "appendEmployees: firestoreDocument.get() ", e);
        });
    }

    public void removeEmployees(Activity activity, String rentManagementId, Set<String> removeEmployeeSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                List<String> prevEmployees = (ArrayList<String>) getTask.getResult().get(RENTMANAGEMENT_EMPLOYEES_FIELD_STRING);
                if(prevEmployees.removeAll(removeEmployeeSet)) {
                    firestoreDocument.update(RENTMANAGEMENT_EMPLOYEES_FIELD_STRING, prevEmployees)
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.i(TAG, "removeEmployees: firestoreDocument.update() removed employees " + removeEmployeeSet);
                                    Toast.makeText(activity, "Employees removed", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "removeEmployees: firestoreDocument.update() ", e);
                            });
                } else {
                    Toast.makeText(activity, "Employees not present", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "removeEmployees: rentManagement document doesn't exists with id " +rentManagementId);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "removeEmployees: firestoreDocument.get() ", e);
        });
    }

    //rooms list service
    public void appendRoom(Activity activity, String rentManagementId, String newRoomId){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()) {
                firestoreDocument.update(RENTMANAGEMENT_ROOMS_FIELD_STRING, FieldValue.arrayUnion(newRoomId))
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Log.i(TAG, "appendRoom: firestoreDocument.update() appended roomID " +newRoomId);
                                Toast.makeText(activity, "RoomID added", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(e -> {
                            Log.e(TAG, "appendRoom: firestoreDocument.update() ", e);
                        });
            } else {
                Log.e(TAG, "appendRoom: rentManagement document doesn't exists with id " +rentManagementId);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "appendRoom: firestoreDocument.get() ", e);
        });
    }

    public void removeRoom(Activity activity, String rentManagementId, String removeRoomId){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                firestoreDocument.update(RENTMANAGEMENT_ROOMS_FIELD_STRING, FieldValue.arrayRemove(removeRoomId))
                        .addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Log.i(TAG, "removeRoom: firestoreDocument.update() removed roomId " + removeRoomId +" from rentmangement");
                            } else {
                                Log.e(TAG, "removeRooms: firestoreDocument.update() failed");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "removeRooms: firestoreDocument.update() ", e);
                        });
            } else {
                Log.e(TAG, "removeRoom: rentManagement document doesn't exists with id " +rentManagementId);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "removeRoom: firestoreDocument.get() ", e);
        });
    }
}
