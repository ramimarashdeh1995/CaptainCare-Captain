package com.captaincare.captan_care.Notification;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.captaincare.captan_care.Activities.MainActivity;
import com.captaincare.captan_care.Activities.SplachActivity;
import com.captaincare.captan_care.Manegers.ShareProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    RecivePushNotification(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            } else {
                // Handle message within 10 seconds
              //  handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void RecivePushNotification(JSONObject json) {
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String type=data.getString("type");
            String ID_Offer=data.getString("offer_id");


            if (ShareProfileData.getInstance(getApplicationContext()).IsLogInCaptain()){
                MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), SplachActivity.class);

                if (type.equals("offer")){
                    mNotificationManager.showOffer(title, message, imageUrl,type,ID_Offer,data.getString("name"),
                            data.getString("address"),data.getString("rank"),
                            data.getString("hour"),data.getString("minute"),data.getString("price"),
                            data.getString("userid"),
                            data.getString("ven_lon"),data.getString("ven_lat"),intent);

                }
                else if (type.equals("acceptoffercaptain")){
                mNotificationManager.showAcceptOfferCaptain(title, message, imageUrl,type,ID_Offer,
                        data.getString("userid"),data.getString("address"),
                        data.getString("offer_title"),data.getString("lon"),
                        data.getString("lat"),data.getString("price"),
                        data.getString("hour"),data.getString("minute"),intent);
                }
                else if (type.equals("rank")){
                      mNotificationManager.showNotificationRank(title,imageUrl,type,ID_Offer,intent);
                }
            }else{
                MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
                ShareProfileData.getInstance(getApplicationContext()).LogInCaptain(
                        ID_Offer,message,imageUrl
                );
                ShareProfileData.getInstance(getApplicationContext()).UpdateProfileCaptain(
                        title,
                        message,
                        "null",
                        "null"
                );
            ShareProfileData.getInstance(getApplicationContext()).SetReatAndPoint(
                        "null",
                        "null"
                );
                ShareProfileData.getInstance(getApplicationContext()).LOGIN("1");

                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                mNotificationManager.showSmallNotification(title,message,imageUrl,ID_Offer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "Refreshed token: " + s);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
       // sendRegistrationToServer(token);
    }
}
