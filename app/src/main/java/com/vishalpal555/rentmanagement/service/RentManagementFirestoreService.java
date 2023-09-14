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
    private static final String RENTMANAGEMENT_EMPLOYEES_FIELD_STRING = "employees";
    private static final String RENTMANAGEMENT_ROOMS_FIELD_STRING = "roomsList";
    private static final String RENTMANAGEMENT_TENANTS_FIELD_STRING = "tenantsList";

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

    //admins service
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
        removeAdminSet.remove(rentManagementId);    //removing default admin id from removeAdminSet
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

    //employees service
    public void appendEmployees(Activity activity, String rentManagementId, Set<String> newEmployeeSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()) {
                List<String> prevEmployees = (ArrayList<String>) getTask.getResult().getData().get(RENTMANAGEMENT_EMPLOYEES_FIELD_STRING);
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
                Log.e(TAG, "appendEmployees: document doesn't exists ");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "appendEmployees: firestoreDocument.get() ", e);
        });
    }

    public void removeEmployees(Activity activity, String rentManagementId, Set<String> removeEmployeeSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                List<String> prevEmployees = (ArrayList<String>) getTask.getResult().getData().get(RENTMANAGEMENT_EMPLOYEES_FIELD_STRING);
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
                Log.e(TAG, "removeEmployees: document doesn't exists ");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "removeEmployees: firestoreDocument.get() ", e);
        });
    }

    //rooms list service
    public void appendRooms(Activity activity, String rentManagementId, Set<String> newRoomSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()) {
                List<String> prevRooms = (ArrayList<String>) getTask.getResult().getData().get(RENTMANAGEMENT_ROOMS_FIELD_STRING);
                if(newRoomSet.addAll(prevRooms)) {
                    firestoreDocument.update(RENTMANAGEMENT_ROOMS_FIELD_STRING, new ArrayList<>(newRoomSet))
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.i(TAG, "appendRooms: firestoreDocument.update() added rooms" + newRoomSet);
                                    Toast.makeText(activity, "Rooms added", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "appendRooms: firestoreDocument.update() ", e);
                            });
                } else {
                    Toast.makeText(activity, "Rooms already present", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "appendRooms: document doesn't exists ");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "appendRooms: firestoreDocument.get() ", e);
        });
    }

    public void removeRooms(Activity activity, String rentManagementId, Set<String> removeRoomSet){
        firestoreDocument = firestoreCollection.document(rentManagementId);
        firestoreDocument.get().addOnCompleteListener(getTask -> {
            if(getTask.getResult().exists()){
                List<String> prevRooms = (ArrayList<String>) getTask.getResult().getData().get(RENTMANAGEMENT_ROOMS_FIELD_STRING);
                if(prevRooms.removeAll(removeRoomSet)) {
                    firestoreDocument.update(RENTMANAGEMENT_ROOMS_FIELD_STRING, prevRooms)
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    Log.i(TAG, "removeRooms: firestoreDocument.update() removed rooms " + removeRoomSet);
                                    Toast.makeText(activity, "Rooms removed", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "removeRooms: firestoreDocument.update() ", e);
                            });
                } else {
                    Toast.makeText(activity, "Rooms not present", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "removeRooms: document doesn't exists ");
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "removeRooms: firestoreDocument.get() ", e);
        });
    }

}
