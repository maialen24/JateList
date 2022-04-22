package com.example.jatelist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

            Log.i("fcm ",remoteMessage.getData().toString());
          //  Toast.makeText(getApplicationContext(),"FCM message",Toast.LENGTH_SHORT).show();
        }
        if (remoteMessage.getNotification() != null) {

            //Toast.makeText(getApplicationContext(),"FCM message",Toast.LENGTH_SHORT).show();

        }
    }






    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        token=FirebaseInstanceId.getInstance().getToken();
        //gorde new token in db
    }


}
