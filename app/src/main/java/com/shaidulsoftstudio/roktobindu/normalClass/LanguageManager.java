package com.shaidulsoftstudio.roktobindu.normalClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageManager {
    private Context ct;
    private SharedPreferences sharedPreferences;

    public LanguageManager(Context ctx) {

        ct = ctx;
        sharedPreferences = ct.getSharedPreferences("Lang", Context.MODE_PRIVATE);

    }

    public void updateResourse(String code) {
        Resources resources = ct.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(code);
        resources.updateConfiguration(configuration, displayMetrics);
        setLanguage(code);
    }

    public String getLanguage(){

      return sharedPreferences.getString("language","en");
    }

    public void setLanguage(String code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", code);
        editor.commit();
    }

}
