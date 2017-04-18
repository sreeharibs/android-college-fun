package com.vkcet.vkcet;


import de.hdodenhof.circleimageview.CircleImageView;

public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = "http://codesquad.in/include/login.php?admNo=";
    public static  final String SET_PASSWORD = "Password";
    public static  final String SET_ADMISSION_NUMBER = "Admission Number";

    public static final String JSON_ARRAY = "result";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_ADM = "admNo";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the details of current logged in user
    public static final String ADM_SHARED_PREF      = "admNo";
    public static final String PWD_SHARED_PREF      = "password";
    public static final String ROLE_SHARED_PREF     = "role";
    public static final String NAME_SHARED_PREF     = "name";
    public static final String PHONE_SHARED_PREF    = "phone";
    public static final String EMAIL_SHARED_PREF    = "email";
    public static final String BATCH_SHARED_PREF    = "batch";
    public static final String DEP_SHARED_PREF      = "dep";
    public static final String WORK_SHARED_PREF     = "work";
    public static final String IMG_SHARED_PREF      = "null";
    public static final String ADDR_SHARED_PREF     = "address";
    public static final String CMPNY_SHARED_PREF    = "company";
    public static final String INDUS_SHARED_PREF    = "industry";
    public static final String PRIVC_SHARED_PREF    = "privacy";




    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
