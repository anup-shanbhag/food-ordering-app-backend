package com.upgrad.FoodOrderingApp.service.common;

import org.apache.commons.lang3.StringUtils;

public class AppConstants {

    // Regular Expression to check if password has at least 1 uppercase letter
    public static final String REG_EXP_PASSWD_UPPER_CASE_CHAR = "^.*[A-Z].*$";
    // Regular Expression to check  if password has at least 1 digit
    public static final String REG_EXP_PASSWD_DIGIT = "^.*[0-9].*$";
    // Regular Expression to check  if password has at least 1 special character.
    public static final String REG_EXP_PASSWD_SPECIAL_CHAR = "^.*[\\#\\@\\$\\%\\&\\*\\!\\^].*$";
    // Regular Expression to check  if email is valid
    public static final String REG_EXP_VALID_EMAIL = "^[a-zA-Z0-9]{3,3}[\\@]{1,1}[a-zA-Z0-9]{2,2}[\\.]{1,1}[a-zA-Z0-9]{2,2}$";

    public static final int TWO_2 = 2;
    public static final Integer SEVEN_7 = 7;
    public static final Integer EIGHT_8 = 8;
    public static final Integer NUMBER_10 = 10;
    public static final String COLON = ":";
    public static final String HTTP_ACCESS_TOKEN_HEADER = "access-token";

}
