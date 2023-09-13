package com.vishalpal555.rentmanagement.global;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String REGISTER_MANUALLY = "Register Manually";
    public static final String HIDE_MANUAL_REG_STRING = "Hide Manual Registration";
    public static final int SUCCESS = 0;
    public static final int CANCELLED = 900;
    public static final int FAIL = 999;
    public static final long TIMEOUT_5_MIN = 5;
    public static final int USER_COLLISION = 901;
    public static final String EMAIL_SENT_EXTRA_KEY = "emailSentExtraKey";
    public static final String GMAIL_LINK = "https://mail.google.com";
    public static final boolean isMock = true;
    public static final String RENT_MANAGEMENT_FIREBASE_REF = "rent_management";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final String FIREBASE_SERVER_REF = "https://rent-management-2bb30-default-rtdb.asia-southeast1.firebasedatabase.app";
}
