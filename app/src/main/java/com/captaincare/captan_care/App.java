package com.captaincare.captan_care;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.captaincare.captan_care.Language.LocaleHelpar;

public class App extends Application {

    public static final String CHANEEL_ID="channel";

    @Override
    public void onCreate() {
        super.onCreate();
        CreateNotificationChanel();
     //   OpienNotificationSetting();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelpar.onContext(base,"en"));

    }

    private void CreateNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(
                    CHANEEL_ID,
                    "Captain Care",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("This is Channel 1");
//           notificationChannel.setSound(Uri.parse((("android.resource://"+ getPackageName() + "/" +R.raw.soundnotification ))));
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private void OpienNotificationSetting(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Intent intent=new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE,getPackageName());
            startActivity(intent);
        }else {
            Intent intent=new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.setData(Uri.parse("package:"+getPackageName()));
            startActivity(intent);
        }
    }
}
