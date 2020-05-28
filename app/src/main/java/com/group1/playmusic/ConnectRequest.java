package com.group1.playmusic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class ConnectRequest {
    static class HttpRequestGet extends AsyncTask<String,String,String> {

        private Context context;

        public HttpRequestGet(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                StringBuffer dataout = new StringBuffer();
                if(http.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                    String inline = "";
                    while ((inline=bufferedReader.readLine()) != null){
                        dataout.append(inline.replace("<br>","\n"));
                    }
                    return dataout.toString();
                }else {
                    Log.d(getClass().getName(), "doInBackground: "+ http.getResponseCode());
                    return "Error";
                }
            } catch (MalformedURLException e) {
                Log.d(getClass().getName(), "doInBackground: "+ e.getMessage());
                return "Error";
            } catch (IOException e) {
                Log.d(getClass().getName(), "doInBackground: "+ e.getMessage());
                return "error";
            }
        }
    }
    static class HttpRequestPost extends AsyncTask<String,String,String> {

        private Context context;

        public HttpRequestPost(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setDoOutput(true);
                http.setRequestMethod("POST");
                try(OutputStreamWriter stream = new OutputStreamWriter(http.getOutputStream())){
                    stream.write(strings[1]);
                }catch (Exception ex){
                    Log.d(getClass().getName(), "doInBackground: "+ ex.getMessage());
                    return "Error";
                }
                StringBuffer dataout = new StringBuffer();
                if(http.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                    String inline = "";
                    while ((inline=bufferedReader.readLine()) != null){
                        dataout.append(inline);
                    }
                    return dataout.toString();
                }else {
                    Log.d(getClass().getName(), "doInBackground: " +http.getResponseCode());
                    return "Error";
                }
            } catch (MalformedURLException e) {
                Log.d(getClass().getName(), "doInBackground: "+ e.getMessage());
                return "Error";
            } catch (IOException e) {
                Log.d(getClass().getName(), "doInBackground: "+ e.getMessage());
                return "error";
            }
        }
    }
    public static String Get(Context context,String url){
        HttpRequestGet httpRequest = new HttpRequestGet(context);
        try {
            String a = httpRequest.execute(url).get();
            return  a;
        } catch (ExecutionException e) {
            Log.d("GET", "doInBackground: "+ e.getMessage());
            return "Error";
        } catch (InterruptedException e) {
            Log.d("GET", "doInBackground: "+ e.getMessage());
            return "Error";
        }
    }
    public static String Post(Context context,String url,String postData){
        HttpRequestPost httpRequest = new HttpRequestPost(context);
        try {
            String a = httpRequest.execute(url,postData).get();
            return  a;
        } catch (ExecutionException e) {
            Log.d("POST", "doInBackground: "+ e.getMessage());
            e.printStackTrace();
            return "Error";
        } catch (InterruptedException e) {
            Log.d("POST", "doInBackground: "+ e.getMessage());
            e.printStackTrace();
            return "Error";
        }
    }
}
