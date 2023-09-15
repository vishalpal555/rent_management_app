package com.vishalpal555.rentmanagement.service;

import android.app.Activity;

public class AdminService {
    private RentManagementFirestoreService rentManagementFirestoreService;
    private RoomsFirestoreService roomsFirestoreService;
    public AdminService(){
        rentManagementFirestoreService = new RentManagementFirestoreService();
        roomsFirestoreService = new RoomsFirestoreService();
    }

    public void removeRoom(Activity activity, String userForeignKey){

    }
}
