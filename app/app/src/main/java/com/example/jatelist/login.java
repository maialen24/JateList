package com.example.jatelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class login extends AppCompatActivity implements DialogClass.Listener, settingsDialog.Listener,infoDialog.Listener{

    /* This class control login activity is used to login, sign up and to set preferences via toolbar */

    private String user;
    private String language;
    private Boolean night;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private SharedPreferences sharedpreferences;
    private Boolean spNight;
    private Boolean notifications;
    private String spLanguage;

    private Boolean recordatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Log.i("CONTROL","ON CREATE");

        //get user preferences

        preferences();
        if (savedInstanceState!= null) {

            user = savedInstanceState.getString("user");
            language =savedInstanceState.getString("language");
            night=savedInstanceState.getBoolean("mode");
            notifications=savedInstanceState.getBoolean("notifications");
            recordatorio=savedInstanceState.getBoolean("recordatorio");

        }else{

            language=spLanguage;
            night=spNight;
         //   onSaveInstanceState(savedInstanceState);
            changeTheme();
          //  changeLanguage();
      /*      SharedPreferences.Editor myEdit = sharedpreferences.edit();

            // Storing the key and its value as the data fetched from edittext
            myEdit.putBoolean("firstTime", firstTime);

            // Once the changes have been made,
            // we need to commit to apply those changes made,
            // otherwise, it will throw an error
            myEdit.commit();*/



        }






        setContentView(R.layout.activity_login);

        //get elements
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        TextView eu=(TextView) findViewById(R.id.euskera);
        TextView en=(TextView) findViewById(R.id.ingles);
        TextView es=(TextView) findViewById(R.id.español);

        EditText userEditText=findViewById((R.id.username));
        EditText password=findViewById((R.id.password));



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("user");
        }


        // get database instance
        db dbHelper = new db(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //login button on click, CHECK CREDENTIALS
        Button login= (Button)findViewById((R.id.loginButton));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info","CHECK USERNAME AND PASSWORD");
                Log.i("info",userEditText.getText().toString());
                Log.i("info",password.getText().toString());
               /* if (dbHelper.checkCredentials(userEditText.getText().toString(), password.getText().toString())){
                    //GO TO MAIN ACTIVITY
                    user=userEditText.getText().toString();
                    Log.i("info","LOGIN SUCCESS");
                    Intent i = new Intent (login.this, MainActivity.class);
                    i.putExtra("user",user);
                    startActivity(i);
                }else{
                    //SHOW A INCORRET CREDENTIALS ALERT
                    Log.i("info","LOGIN INCORRECT");
                    AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                    builder.setTitle("Error");
                    builder.setMessage(v.getContext().getString(R.string.login_mssg));
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }*/
                check();


            }
        });



        //euskera button (textView) clicked, change language
        eu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Euskera");
                language="eu";
                spNight=night;
                changeLanguage();
                changeTheme();
                finish();
                startActivity(getIntent());
            }
        });

        // english button (textview) clicked change language
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Ingelesa");
                language="en";
                spNight=night;
                changeLanguage();
                changeTheme();
                finish();
                startActivity(getIntent());
                //onStart();
            }
        });

        //spanish button (textview) clicked change language
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info-paso","Castellano");
                language="es";
                spNight=night;
                changeLanguage();
                changeTheme();
                finish();
                startActivity(getIntent());
               // onStart();
            }
        });



        // sign up button, shows sing up dialog (DialogClass)
        ImageButton newUser= (ImageButton)findViewById((R.id.signUp));
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogoalerta= new DialogClass();
                dialogoalerta.show(getSupportFragmentManager(), "etiqueta");

            }
        });

        db.close();
        dbHelper.close();
    }


    // implements alpulsarSI method from DialogClass listener interface
    // on click positive button in DialogClass, sing up dialog
    @Override
    public void alpulsarSI(String user, String password) {
        //SAVE USER AND PASSWORD IN DB
        Log.i("info","AL PULSAR SI METODO");
        Log.i("info",user);
        Log.i("info",password);

        //insert new user in db
        db dbHelper = new db(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        //dbHelper.insertUser(user, password);
        insert(user,password);

        // Sends local notification with user ok created message
        NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(this, "1");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel elCanal = new NotificationChannel("1", "login",
                    NotificationManager.IMPORTANCE_DEFAULT);
            elManager.createNotificationChannel(elCanal);
            elCanal.setDescription("Descripción del canal");
            elCanal.enableLights(true);
            elCanal.setLightColor(Color.RED);
            elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            elCanal.enableVibration(true);
        }
        elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Sign up")
                .setContentText(getString(R.string.usercreate_mssg))
                .setSubText("Users")
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setAutoCancel(true);
        elManager.notify(1, elBuilder.build());

        dbHelper.close();



    }

    //implements alpusarNO method of DialogClass interface
    @Override
    public void alpulsarNO() {
        //CLOSE DIALOG

    }



    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("user",user );
        savedInstanceState.putString("language",language );
        savedInstanceState.putBoolean("modo",night );



    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = savedInstanceState.getString("user");
        language = savedInstanceState.getString("language");
        night = savedInstanceState.getBoolean("modo");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {

            // click info option, shows infoDialog
            case R.id.action_about:
                DialogFragment dialogAbout= new infoDialog();
                dialogAbout.show(getSupportFragmentManager(), "info dialog");
                return true;

                //click settings option, shows settingsDialog
            case R.id.action_settings:
                Bundle bundle = new Bundle();
                bundle.putString("language",spLanguage);
                bundle.putBoolean("mode",spNight);
                bundle.putBoolean("noti",notifications);
                bundle.putBoolean("notir",recordatorio);

                DialogFragment dialogSettings= new settingsDialog();
                dialogSettings.setArguments(bundle);
                dialogSettings.show(getSupportFragmentManager(), "settings dialog");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    //implements alpulsarSave method of settingsDialog interface
    // save user preferences
    @Override
    public void alpulsarSave(Boolean pmode, boolean noti,boolean notir) {
        //save preferences (settings dialog)
        SharedPreferences.Editor myEdit = sharedpreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putBoolean("mode", pmode);
        myEdit.putBoolean("notifications", noti);
        myEdit.putBoolean("recordatorio", notir);
        notifications=noti;
        recordatorio=notir;
        night=pmode;
        changeTheme();

        if(recordatorio){
            FirebaseMessaging.getInstance().subscribeToTopic("recordatorio")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }

                            Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic("recordatorio")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_unsubscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_unsubscribe_failed);
                            }

                            Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        if(notifications){
            FirebaseMessaging.getInstance().subscribeToTopic("new")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }

                            Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic("new")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_unsubscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_unsubscribe_failed);
                            }

                            Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.commit();
    }

    // implements infoDialog interface positive button method, close dialog
    @Override
    public void alpulsarOK() {
        //close info dialog
    }

    // metodo laguntzaile to save preferences
    private void preferences(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        spNight = sharedpreferences.getBoolean("mode",false);
        notifications = sharedpreferences.getBoolean("notifications",false);
        recordatorio = sharedpreferences.getBoolean("recordatorio",false);
        language=spLanguage;
     //   firstTime = sharedpreferences.getBoolean("firstTime",true);

    }

    //metodo laguntzaile to change language
    private void changeLanguage(){

        Locale nuevaloc = new Locale(language);
        Locale.setDefault(nuevaloc);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.setLocale(nuevaloc);
        configuration.setLayoutDirection(nuevaloc);

        Context context = getBaseContext().createConfigurationContext(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());



    }

    //metodo laguntzaile to change app mode
    private void changeTheme(){
        if(night){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i("CONTROL","login ON PAUSE");

    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("CONTROL"," login ON START");


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("CONTROL","login ON RESUME");


    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.i("CONTROL","login ON STOP");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CONTROL","login ON DESTROY");

    }


public void check(){
    final Boolean[] emaitza = {false};
    Data.Builder data = new Data.Builder();
    //Se introducen los datos necesarios a pasar a ConexionPHP
    EditText userEditText=(EditText) findViewById(R.id.username);
    EditText password=(EditText) findViewById(R.id.password);
    data.putString("user",userEditText.getText().toString());
    data.putString("password",password.getText().toString());
    data.putString("funcion","check");

    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(usersPHPconnect.class).setInputData(data.build()).build();
    WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
            .observe(this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo)
                {
                    //Si se puede iniciar sesión porque devulve true se cambiará la actividad cerrando en la que se encuentra. Si la devolución es null o no es true se mostrará un toast en la interfaz actual.
                    if(workInfo != null && workInfo.getState().isFinished())
                    {
                        String emaitza = workInfo.getOutputData().getString("result");
                        if (emaitza!=null) {
                            if (emaitza.equals("true")) {
                                //GO TO MAIN ACTIVITY
                                user=userEditText.getText().toString();
                                Log.i("info","LOGIN SUCCESS");
                                Intent i = new Intent (login.this, MainActivity.class);
                                i.putExtra("user",user);
                                startActivity(i);
                            } else {
                                //SHOW A INCORRET CREDENTIALS ALERT
                                Log.i("info","LOGIN INCORRECT");
                                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                                builder.setTitle("Error");
                                builder.setMessage(getString(R.string.login_mssg));
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            }
                        }
                        else {
                            //SHOW A INCORRET CREDENTIALS ALERT
                            Log.i("info","LOGIN INCORRECT");
                            AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                            builder.setTitle("Error");
                            builder.setMessage(getString(R.string.login_mssg));
                            builder.setPositiveButton("OK", null);
                            builder.show();
                        }
                    }
                }
            });
    WorkManager.getInstance(this).enqueue(otwr);


}

public void insert(String user, String password){
    final Boolean[] emaitza = {false};
    Data.Builder data = new Data.Builder();
    //Se introducen los datos necesarios a pasar a ConexionPHP

    data.putString("user",user);
    data.putString("password",password);
    data.putString("funcion","insert");

    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(usersPHPconnect.class).setInputData(data.build()).build();
    WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
            .observe(this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(WorkInfo workInfo)
                {
                    //Si se puede iniciar sesión porque devulve true se cambiará la actividad cerrando en la que se encuentra. Si la devolución es null o no es true se mostrará un toast en la interfaz actual.
                    if(workInfo != null && workInfo.getState().isFinished())
                    {
                        String emaitza = workInfo.getOutputData().getString("result");
                        if (emaitza!=null) {
                            if (emaitza.equals("true")) {
                                Log.i("user","created");
                            }
                        }
                    }
                }
            });
    WorkManager.getInstance(this).enqueue(otwr);
}


}


