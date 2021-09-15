package com.mobdeve.s18.cuevas.alfonso.legitcheckers.util;

import android.content.Context;
import android.content.SharedPreferences;

public class StoragePreferences {
    private SharedPreferences preferences;
    private final String PREFS = "preferences";

    public StoragePreferences(Context context){
        preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void saveMusicPreferences(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public boolean getMusicPreferences(String key){
        return (preferences.getBoolean(key, true));
    }

    public void saveThemePreferences(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public boolean getThemePreferences(String key){
        return (preferences.getBoolean(key, false));
    }

}
