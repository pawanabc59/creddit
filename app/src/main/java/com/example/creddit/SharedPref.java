package com.example.creddit;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    SharedPreferences mySharedPref;
    public Context context;
    public SharedPreferences.Editor editor;

    public SharedPref(Context context){
        this.context = context;
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

//    this method will save the nightmode State : True or False
    public void setNightModeState(Boolean state){
        editor = mySharedPref.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

//    this method will load the night mode state
    public Boolean loadNightModeState(){
        Boolean state = mySharedPref.getBoolean("NightMode", false);
        return state;
    }

}
