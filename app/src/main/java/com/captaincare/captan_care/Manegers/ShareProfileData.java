package com.captaincare.captan_care.Manegers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class ShareProfileData {
    @SuppressLint("StaticFieldLeak")
    private static ShareProfileData mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static final String SHARED_PREF_NAME_LOG = "SharingProfile";
    private static final String KeyID = "key_id";
    private static final String KeyLic_URL = "key_url";
    private static final String KeyPhone2= "cap_phone";
    private static final String KeySerialID= "key_serialID";


    private static final String SHARED_PREF_NAME = "shareprofilecaptain";
    private static final String KeyName = "cap_name";
    private static final String KeyPhone = "cap_phone";
    private static final String Keycap_username = "cap_username";
    private static final String KeyCity = "cap_city";
    private static final String Keyprofile_URL = "cap_url";

    private static final String KeyRate="cap_reat";
    private static final String KeyPoint="point";


    private static final String SHARED_PREF_NAME_FCM = "FCMSharedToken";
    private static final String TAG_TOKEN = "tagtoken";

    private static final String SHARED_PREF_NAME_Log_in = "logincaptain";
    private static final String TAG_LOGIN= "log";

    private static final String KeyLanguage = "key_lang";


    private ShareProfileData(Context context) {
        ShareProfileData.context = context;
    }

    public static synchronized ShareProfileData getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ShareProfileData(context);
        }
        return mInstance;
    }

    public static void setLanguage(Context activity, String lang) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyLanguage,lang);
        editor.apply();
    }
    public static String getLanguage(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyLanguage,"en");
    }

    public static void setSerialID(Context activity, String serialID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeySerialID,serialID);
        editor.apply();
    }
    public static String getSerialID(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeySerialID,"");
    }


    public void LogInCaptain(String id, String mobile,String lic_url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyID, id);
        editor.putString(KeyPhone2, mobile);
        editor.putString(KeyLic_URL, lic_url);

        editor.apply();

    }

    public boolean IsLogInCaptain() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyPhone, null) != null;
    }

    public void LogoutPROFILE() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void UpdateProfileCaptain(String name, String mobile, String city, String img_url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyName, name);
        editor.putString(KeyPhone, mobile);
        editor.putString(KeyCity, city);
        editor.putString(Keyprofile_URL,img_url);

        editor.apply();

    }

    public void SetReatAndPoint( String rate, String point) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyRate, rate);
        editor.putString(KeyPoint, point);

        editor.apply();

    }

    public void Logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getKEYID() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_LOG, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyID, null);
    }
    public  String getKeyName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyName, null);
    }

    public  String getKeyPhone() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyPhone, null);
    }

    public  String getKeyCity() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyCity, null);
    }

    public  String getKeyprofile_URL() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Keyprofile_URL, null);
    }

    public  String getKeyRate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyRate, null);
    }

    public  String getKeyPoint() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KeyPoint, null);
    }


    public void saveDeviceToken(String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_FCM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_FCM, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    public void LOGIN(String state){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_Log_in, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_LOGIN, state);
        editor.apply();
    }
    public boolean ISLOGIN() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_Log_in, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_LOGIN, null) != null;
    }

    public void LogoutSTATE() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME_Log_in, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
