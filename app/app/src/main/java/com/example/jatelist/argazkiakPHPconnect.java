package com.example.jatelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
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
//Esta clase es la que llama al php relacionado con el almacenamiento de fotos y gestiona los resultados
public class argazkiakPHPconnect extends Worker {
    public argazkiakPHPconnect(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    /* Este metodo llama a las funciones de insertar o get dependiendo de los parametros recibidos */
    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        String funcion=getInputData().getString("funcion");
        String user = getInputData().getString("user");
        String argazkia = getInputData().getString("foto");
        String izena = getInputData().getString("izena");
        if(funcion.equals("insert")){
            return insert(user,argazkia,izena);
        }else{
            return get(user,izena);
        }


    }
    /* Metodo que gestiona el insertar fotos en la db remota  */
    public ListenableWorker.Result insert(String user,  String  image, String izena ){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String funcion= "insert";
        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/argazkiak.php";
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
            String parametros = "funcion="+funcion+"&user="+user+"&izena="+izena+"&foto="+image;
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

                    resultado = jsonArray.getJSONObject(i).getString("resultado");
                }
                Data json = new Data.Builder()
                        .putString("result",resultado)
                        .build();
                Log.i("php","json: " + json);
                return ListenableWorker.Result.success(json);
            }
            return ListenableWorker.Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return ListenableWorker.Result.failure();
    }

    /* Metodo que gestiona la extraccion de fotos de la db remota */
    public ListenableWorker.Result get(String user, String izena){

        String funcion= "get";

        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/argazkiak.php";
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
            String parametros = "funcion="+funcion+"&user="+user+"&izena="+izena;
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("STATUS PHP get image","statusCode: " + urlConnection);
            int statusCode = urlConnection.getResponseCode();
            Log.i("STATUS PHP get image","statusCode: " + statusCode);
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
                    Log.i("JSONImagenes", "doWork: "+jsonArray.getJSONObject(i));
                    resultado = jsonArray.getJSONObject(i).getString("resultado");
                    Log.i("JSONImagenes", "doWork: "+resultado);
                }
                Data json = new Data.Builder()
                        .putString("foto",resultado)
                        .build();
                return Result.success(json);
            }
            return Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return ListenableWorker.Result.failure();
    }
}

