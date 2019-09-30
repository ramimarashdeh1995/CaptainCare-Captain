package com.captaincare.captan_care.Language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelpar {

    private static final String SELECTED_LANGOUGE="Locale.Helper.Selected.langauge";

    public static Context onContext (Context context){
        String lan=getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocal(context,lan);
    }

    public static Context onContext (Context context,String defultLangauge){
        String lan=getPersistedData(context, defultLangauge);
        return setLocal(context,lan);
    }

    public static Context setLocal(Context context, String lan) {
        Persist(context,lan);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        return UpdateRecourseLang(context,lan);
       return UpdateResource(context,lan);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context UpdateResource(Context context, String lan) {
        Resources resource=context.getResources();

        Configuration configuration=resource.getConfiguration();
        configuration.setLocale(new Locale(lan));
        configuration.setLayoutDirection(new Locale(lan));

        resource.updateConfiguration(configuration,resource.getDisplayMetrics());

        return context;
    }

    private static Context UpdateRecourseLang(Context context, String lan) {
        Resources resource=context.getResources();

        Configuration configuration=resource.getConfiguration();
        configuration.setLocale(new Locale(lan));
        configuration.setLayoutDirection(new Locale(lan));

        resource.updateConfiguration(configuration,resource.getDisplayMetrics());

        return context;

    }

    private static void Persist(Context context,String lang){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString(SELECTED_LANGOUGE,lang);
        editor.apply();
    }

    private static String getPersistedData(Context context, String language) {
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGOUGE,language);
    }
}
