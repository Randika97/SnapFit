package com.example.snapfit;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snapfit.model.Users;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String email){
        //save session of user whenever user is logged in
        editor.putString(SESSION_KEY,email).commit();
    }

    public String getSession(){
        //return user id whose session is saved
        return sharedPreferences.getString(SESSION_KEY,null);
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,null).commit();
    }
}
