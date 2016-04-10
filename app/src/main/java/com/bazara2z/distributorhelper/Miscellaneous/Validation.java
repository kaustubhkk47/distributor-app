package com.bazara2z.distributorhelper.Miscellaneous;

/**
 * Created by Maddy on 06-04-2016.
 */
public class Validation {
    public boolean isPincodeValid(String pincode){
        if (pincode.matches("[0-9]+") && pincode.length() == 6){
            return true;
        }
        return false;
    }

    public boolean isPhoneNoValid(String phoneno) {
        if (phoneno.matches("[0-9]+") && phoneno.length() == 10 && phoneno.substring(0, 1).matches("9|8|7")){
            return true;
        }
        return false;
    }

    public boolean isPasswordValid(String password) {
        return true;
    }
}
