package com.example.jatelist;

import android.content.Context;
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
/*  Clase gestora del archivo php relacionado con la tabla jatetxea de la db remota */
public class jatetxeakPHPconnect extends Worker {
    public jatetxeakPHPconnect(@NonNull Context context, @NonNull WorkerParameters workerParams)
    {
        super(context, workerParams);
    }

    /* Metodo que gestiona las llamadas a las diferentes funciones segun parametros recibidos */
    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        String funcion=getInputData().getString("funcion");


        if(funcion.equals("insert")){
            return insert();
        }else if(funcion.equals("delete")){
            return delete();
        }else if(funcion.equals("update")){
            return update();
        }else if(funcion.equals("get")){
            return getAll();
        }

        return ListenableWorker.Result.failure();

    }

/* Llamada a la funcion insert del php para el almacenamiento de restaurantes */
    public ListenableWorker.Result insert(){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String user = getInputData().getString("user");
        String name = getInputData().getString("izena");
        String valoracion = getInputData().getString("valoracion");
        String funcion= "insert";
        Log.i("insert jatetxe","jatetxe");

        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/jatetxeak.php";
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
            String parametros = "funcion="+funcion+"&user="+user+"&name="+name+"&valoracion="+valoracion;
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
                return ListenableWorker.Result.success(json);
            }
            return ListenableWorker.Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return ListenableWorker.Result.failure();
    }

    /* Llamada a la funcion update del php que gestiona las actualizaciones del restaurante */
    public ListenableWorker.Result update(){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String funcion= "update";
        String user = getInputData().getString("user");
        String name = getInputData().getString("izena");
        String valoracion = getInputData().getString("rating");


        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/jatetxeak.php";
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
            String parametros = "funcion="+funcion+"&user="+user+"&name="+name+"&valoracion="+valoracion;
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
                Log.i("php","listaJson: " + json);
                return ListenableWorker.Result.success(json);
            }
            return ListenableWorker.Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return ListenableWorker.Result.failure();
    }

    /* Llamada a la funcion delete del php que gestiona el borrado de restaurantes */
    public ListenableWorker.Result delete(){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String funcion= "delete";
        String user = getInputData().getString("user");
        String name = getInputData().getString("izena");

        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/jatetxeak.php";
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
            String parametros = "funcion="+funcion+"&user="+user+"&name="+name;
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("STATUS PHP delete jatetxea","statusCode: " + urlConnection);
            int statusCode = urlConnection.getResponseCode();
            Log.i("STATUS PHP DELETE JATETXEA","statusCode: " + statusCode);
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
                return ListenableWorker.Result.success(json);
            }
            return ListenableWorker.Result.failure();
        }
        catch (MalformedURLException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
        return ListenableWorker.Result.failure();
    }

    /* Llamada */
    public ListenableWorker.Result get(){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String funcion="get";
        String user = getInputData().getString("user");

        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/jatetxeak.php";
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
            String parametros = "funcion="+funcion+"&user="+user;
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("STATUS PHP get all restaurant","statusCode: " + urlConnection);
            int statusCode = urlConnection.getResponseCode();
            Log.i("STATUS PHP get all restaurant","statusCode: " + statusCode);
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

    /* Llamada al metodo get del php para extraer los registros de los restaurantes */
    public ListenableWorker.Result getAll(){
        //String user = getInputData().getString("user");
        //String password = getInputData().getString("password");
        String funcion= "getAll";


        String direccion = "http://ec2-52-56-170-196.eu-west-2.compute.amazonaws.com/mruiz142/WEB/jatetxeak.php";
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
            String parametros = "funcion="+funcion;
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametros);
            out.close();
            Log.i("STATUS PHP GET ALL RESTAURANTS","statusCode: " + urlConnection);
            int statusCode = urlConnection.getResponseCode();
            Log.i("STATUS PHP GET ALL RESTAURANTS","statusCode: " + statusCode);
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
                String izena="";
                String valoracion="";
                for(int i = 0; i < jsonArray.length(); i++)
                {

                    if(izena==""){
                        izena = jsonArray.getJSONObject(i).getString("izena");
                        valoracion = jsonArray.getJSONObject(i).getString("valoracion");
                    }else{
                        izena = izena+","+jsonArray.getJSONObject(i).getString("izena");
                        valoracion = valoracion+","+jsonArray.getJSONObject(i).getString("valoracion");
                    }

                }
                Data json = new Data.Builder()
                        .putString("izena", izena)
                        .putString("valoracion", valoracion)
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
}
