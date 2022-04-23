package com.example.jatelist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class ServicioFirebase extends FirebaseMessagingService {
    private String token;
    public ServicioFirebase() {

}
    public String generarToken(){
        token= FirebaseInstanceId.getInstance().getToken();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>(){
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task){

                if (task.isSuccessful()){
                    token = task.getResult().getToken();
                    Log.i("Token", "ondo "+token);
                }
                else{
                    Log.i("Token", "error");
                }

            }
        });
        return  token;
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
/*
            Log.i("fcm ",remoteMessage.getData().toString());
            NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "2");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel elCanal = new NotificationChannel("2", "mssg",
                        NotificationManager.IMPORTANCE_DEFAULT);
                elManager.createNotificationChannel(elCanal);
                elCanal.setDescription("Firebase messages");
                elCanal.enableLights(true);
                elCanal.setLightColor(Color.RED);
                elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                elCanal.enableVibration(true);
            }
            elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle("Notificacion")
                    .setContentText(remoteMessage.getData().toString())
                    .setSubText(remoteMessage.getData().toString())
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);
            elManager.notify(1, elBuilder.build());*/
            //Toast.makeText(getApplicationContext(),remoteMessage.getData().toString(),Toast.LENGTH_SHORT).show();
        }
        if (remoteMessage.getNotification() != null) {
            Log.i("fcm ","recived");
            //Toast.makeText(getApplicationContext(),"FCM message",Toast.LENGTH_SHORT).show();
            NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "2");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel elCanal = new NotificationChannel("2", "mssg",
                        NotificationManager.IMPORTANCE_DEFAULT);
                elManager.createNotificationChannel(elCanal);
                elCanal.setDescription("Firebase messages");
                elCanal.enableLights(true);
                elCanal.setLightColor(Color.RED);
                elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                elCanal.enableVibration(true);
            }
            elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle("Notificacion")
                    .setContentText(remoteMessage.getNotification().getBody().toString())
                    .setSubText(remoteMessage.getNotification().getBody())
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);
            elManager.notify(1, elBuilder.build());

        }
    }


/*
public void sendmessage(){
    // The topic name can be optionally prefixed with "/topics/".
    String topic = "highScores";

// See documentation on defining a message payload.
    Message message = Message.builder()
            .putData("score", "850")
            .putData("time", "2:45")
            .setTopic(topic)
            .build();

// Send a message to the devices subscribed to the provided topic.
    String response = FirebaseMessaging.getInstance().send(message);
// Response is a message ID string.
    System.out.println("Successfully sent message: " + response);
}*/




    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        token=FirebaseInstanceId.getInstance().getToken();
        //gorde new token in db
    }


}
