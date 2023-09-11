package com.vishalpal555.rentmanagement.service;

import com.vishalpal555.rentmanagement.global.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean isEmail(String email){
        Pattern pattern = Pattern.compile(Constants.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
