package com.example.jatelist;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class usersPHPconnect extends Worker {
    public usersPHPconnect(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String funcion=getInputData().getString("funcion");
        String user = getInputData().getString("user");
        String password = getInputData().getString("password");
        if(funcion.equals("insert")){
            return insertUser(user,password);
        }else{
            return check(user,password);
        }


    }

    public Result insertUser(String user,String password){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String funcion= "insertUser";
        Log.i("insert", "user: "+user);
        Log.i("insert", "password: "+password);
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/users.php";
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
            String parametros = "funcion="+funcion+"&user="+user+"&password="+password;
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("insert","statusCode: " + urlConnection);
            int statusCode = urlConnection.getResponseCode();
            Log.i("insert ","statusCode: " + statusCode);
            if (statusCode == 200)
            {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                JSONArray jsonArray = new JSONArray(result);
                String resultado="";
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Log.i("insert", "doWork: "+jsonArray.getJSONObject(i));
                    resultado = jsonArray.getJSONObject(i).getString("resultado");
                }
                Data json = new Data.Builder()
                        .putString("result",resultado)
                        .build();
                Log.i("php","json: " + json);
                return Result.success(json);
            }
            return Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return Result.failure();
    }

    public Result check(String user,String password){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String funcion= "checkCredentials";
        Log.i("check", "user: "+user);
        Log.i("check", "password: "+password);
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/users.php";
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
            String parametros = "funcion="+funcion+"&user="+user+"&password="+password;
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("STATUS PHP CHECK","statusCode: " + urlConnection);
            int statusCode = urlConnection.getResponseCode();
            Log.i("STATUS PHP CHECK","statusCode: " + statusCode);
            if (statusCode == 200)
            {
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                JSONArray jsonArray = new JSONArray(result);
                String resultado="";
                for(int i = 0; i < jsonArray.length(); i++)
                {
                    Log.i("check", "doWork: "+jsonArray.getJSONObject(i));
                    resultado = jsonArray.getJSONObject(i).getString("resultado");
                }
                Data json = new Data.Builder()
                        .putString("result",resultado)
                        .build();
                Log.i("php","json: " + json);
                return Result.success(json);
            }
            return Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return Result.failure();
    }
}