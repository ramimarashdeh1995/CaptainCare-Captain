package com.captaincare.captan_care.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.captaincare.captan_care.App;
import com.captaincare.captan_care.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyNotificationManager {

    private Context context;
    public static int ID_BIG_NOTIFICATION = 235;
    private static NotificationManager mNotificationManager = null;


    public MyNotificationManager(Context context) {
        this.context = context;
    }

    public void showOffer(String title, String message, String url, String type, String OfferID, String name , String Address,
                          String rate, String hours, String minute, String pirce, String UserID,
                          String lon, String lat, Intent intent){
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent I=new Intent(context,ReciveNotification.class);
        I.putExtra("title",title);
        I.putExtra("message",message);
        I.putExtra("url",url);
        I.putExtra("type",type);
        I.putExtra("OfferID",OfferID);
        I.putExtra("name",name);
        I.putExtra("Address",Address);
        I.putExtra("rate",rate);
        I.putExtra("hours",hours);
        I.putExtra("minute",minute);
        I.putExtra("userID",UserID);
        I.putExtra("cost",pirce);
        I.putExtra("lon",lon);
        I.putExtra("lat",lat);
        I.putExtra("id_notification",ID_BIG_NOTIFICATION);

        PendingIntent reciveNotification =PendingIntent.getActivity(
                context,
                ID_BIG_NOTIFICATION,
                I,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_small_notification);
        remoteViews.setTextViewText(R.id.txtTitle, title);

        RemoteViews bigremotViews=new RemoteViews(context.getPackageName(), R.layout.new_big_notification);
        bigremotViews.setTextViewText(R.id.titleMessageBig,title);
        bigremotViews.setTextViewText(R.id.discraptionBig,message);

        if (hours.equals("null")){
            bigremotViews.setTextViewText(R.id.timeMessagebig,minute+ " M");
        }else {
            bigremotViews.setTextViewText(R.id.timeMessagebig,hours+":"+minute+" H");
        }
        bigremotViews.setTextViewText(R.id.addressnotification,Address);

        bigremotViews.setTextViewText(R.id.cost,pirce+" "+context.getString(R.string.jd));
        if (url.equals("null")){

        }else {
            bigremotViews.setImageViewBitmap(R.id.imagenotificationBig_1,getBitmapFromURL(url));
        }
        if (rate.equals("null")){
            bigremotViews.setTextViewText(R.id.rankuser,"5");
        }else {
            bigremotViews.setTextViewText(R.id.rankuser,rate);
        }
        bigremotViews.setOnClickPendingIntent(R.id.btnAccept,reciveNotification);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, App.CHANEEL_ID)
                .setTicker("Captain Care")
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigremotViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(Uri.parse((("android.resource://"+ context.getPackageName() + "/" +R.raw.soundnotification ))))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification;
        notification = mBuilder.build();

        mNotificationManager.notify(ID_BIG_NOTIFICATION++, notification);
    }

    public void showAcceptOfferCaptain(String name, String rank, String imageUrl, String type,
                                       String id_offer, String userid, String address,String OfferTitle,
                                       String lon,String lat, String cost,
                                       String hours,String minute, Intent intent) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent I=new Intent(context,ReciveNotification.class);
        I.putExtra("name",name);
        I.putExtra("rank",rank);
        I.putExtra("url",imageUrl);
        I.putExtra("type",type);
        I.putExtra("OfferID",id_offer);
        I.putExtra("Address",address);
        I.putExtra("userID",userid);
        I.putExtra("lon",lon);
        I.putExtra("lat",lat);
        I.putExtra("hours",hours);
        I.putExtra("cost",cost);
        I.putExtra("minute",minute);
        I.putExtra("id_notification",ID_BIG_NOTIFICATION);

        PendingIntent reciveNotification =PendingIntent.getActivity(
                context,
                ID_BIG_NOTIFICATION,
                I,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_small_notification);
        remoteViews.setTextViewText(R.id.txtTitle, "Accept your offer");

        RemoteViews bigremotViews=new RemoteViews(context.getPackageName(),R.layout.new_notification_acceptoffercaptain);
        bigremotViews.setTextViewText(R.id.titleMessageBig,name);
        bigremotViews.setTextViewText(R.id.discraptionBig,OfferTitle);


        bigremotViews.setTextViewText(R.id.addressnotification,address);
        if (rank.equals("null")){
            bigremotViews.setTextViewText(R.id.rankuser,"5");
        }else {
            bigremotViews.setTextViewText(R.id.rankuser,rank);
        }

        if (imageUrl.equals("0")){

        }else {
            bigremotViews.setImageViewBitmap(R.id.imagenotificationBig_1,getBitmapFromURL(imageUrl));
        }
        bigremotViews.setOnClickPendingIntent(R.id.btnAccept,reciveNotification);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, App.CHANEEL_ID)
                .setTicker("Captain Care")
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigremotViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(Uri.parse((("android.resource://"+ context.getPackageName() + "/" +R.raw.soundnotification ))))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification;
        notification = mBuilder.build();

        mNotificationManager.notify(ID_BIG_NOTIFICATION++, notification);

    }

    public void showNotificationRank(String name,String imageUrl,String type,String SaveID,Intent intent){
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent I=new Intent(context,ReciveNotification.class);
        I.putExtra("name",name);
        I.putExtra("type",type);
        I.putExtra("SaveID",SaveID);
        I.putExtra("imag",imageUrl);
        I.putExtra("id_notification",ID_BIG_NOTIFICATION);

        PendingIntent reciveNotification =PendingIntent.getActivity(
                context,
                ID_BIG_NOTIFICATION,
                I,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_small_notification);
        remoteViews.setTextViewText(R.id.txtTitle, name);
        remoteViews.setOnClickPendingIntent(R.id.notificationsmall,reciveNotification);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, App.CHANEEL_ID)
                .setTicker("Captain Care")
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(Uri.parse((("android.resource://"+ context.getPackageName() + "/" +R.raw.soundnotification ))))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Notification notification;
        notification = mBuilder.build();

        mNotificationManager.notify(ID_BIG_NOTIFICATION++, notification);
    }

    public void showSmallNotification(String title, String message, String type, String ID_Offer) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, App.CHANEEL_ID)
                .setAutoCancel(true)
                .setContentTitle("Captain Care")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Accept Profile")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Captain Care")
                        .bigText("Accept Your Request To Add in Captain Care "))
                .setSound(Uri.parse((("android.resource://"+ context.getPackageName() + "/" + R.raw.soundnotification ))))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notification;
        notification = mBuilder.build();

        mNotificationManager.notify(ID_BIG_NOTIFICATION++, notification);
    }

    private Bitmap getBitmapFromURL(String URL) {
        try {
            java.net.URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
