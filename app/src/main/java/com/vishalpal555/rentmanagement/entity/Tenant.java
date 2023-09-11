package com.vishalpal555.rentmanagement.entity;

import com.vishalpal555.rentmanagement.service.Validator;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class Tenant {
    private String tenantUsername;
    private String name;
    private int advancePaid;
    private String officialId;
    private LocalDate joiningDate;
    private LocalDate leavingDate;
    private HashMap<LocalDate, Integer> monthlyPaymentAmount;
    private HashMap<String, String> comments;
    private boolean isTenantActive;
    private String phoneNumber;

    public Tenant() {

    }

    public boolean getIsTenantActive(){
        return joiningDate.isBefore(LocalDate.now()) && (leavingDate == null || leavingDate.isAfter(LocalDate.now()));
    }

    public Tenant(String tenantUsername, String name, int advancePaid, String officialId, LocalDate joiningDate, LocalDate leavingDate, String phoneNumber) {
        this.tenantUsername = tenantUsername;
        this.name = name;
        this.advancePaid = advancePaid;
        this.officialId = officialId;
        this.joiningDate = joiningDate;
        this.leavingDate = leavingDate;
        this.monthlyPaymentAmount = new HashMap<>();
        this.comments = new HashMap<>();
        this.setPhoneNumber(phoneNumber);
    }
    public void setPhoneNumber(String phoneNumber){
        Validator validator = new Validator();
        if(phoneNumber != null && phoneNumber.length() == 10 && validator.isNumeric(phoneNumber)){
            this.phoneNumber = phoneNumber;
        }
    }
}
