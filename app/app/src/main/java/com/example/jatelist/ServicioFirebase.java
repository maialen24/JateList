package com.example.jatelist;

import android.app.Service;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {
    public ServicioFirebase() {

}
    public String generarToken(){
        String token="";
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>(){
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task){
                String token;
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
        }
        if (remoteMessage.getNotification() != null) {

        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //gorde new token in db
    }


}
