package com.example.jatelist;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class sendMessagePHPconnect extends Worker {
    public sendMessagePHPconnect(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        String mssg = getInputData().getString("mssg");
        String token = getInputData().getString("token");
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/sendtoTopic.php";
     //   String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/fcm.php";
        HttpURLConnection urlConnection = null;
        try
        {
            URL destino = new URL(direccion);
            urlConnection = (HttpURLConnection) destino.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String parametros = "mssg="+mssg+"&token="+token;
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("TAG","statusCode: " + urlConnection);
            int statusCode = urlConnection.getResponseCode();
            Log.i("TAG","statusCode: " + statusCode);
            if (statusCode == 200)
            {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                Data json = new Data.Builder()
                        .putString("result","true")
                        .build();
                Log.i("php","listaJson: " + json);
                return Result.success(json);
            }
            return Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return Result.failure();
    }
}
