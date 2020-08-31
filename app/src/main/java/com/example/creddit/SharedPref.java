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
        editor = mySharedPref.edit();
    }

//    this method will save the nightmode State : True or False
    public void setNightModeState(Boolean state){
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

//    this method will load the night mode state
    public Boolean loadNightModeState(){
        Boolean state = mySharedPref.getBoolean("NightMode", false);
        return state;
    }

    public void put_showNSFW(int showNSFW){
        editor.putInt("showNSFW", showNSFW);
        editor.commit();
    }

    public int get_showNSFW(){
        int showNSFW = mySharedPref.getInt("showNSFW",0);
        return showNSFW;
    }

    public void put_blurNSFW(int blurNSFW){
        editor.putInt("blurNSFW", blurNSFW);
        editor.commit();
    }

    public int get_blurNSFW(){
        int blurNSFW = mySharedPref.getInt("blurNSFW",0);
        return blurNSFW;
    }

}
