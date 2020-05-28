package com.group1.playmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import com.group1.playmusic.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ToolHelper {

    public static class BitmapFromUrl extends AsyncTask<String,String, Bitmap>{

        public ImageView imageView;
        public Context contextl;

        public BitmapFromUrl(ImageView imageView, Context contextl) {
            this.imageView = imageView;
            this.contextl = contextl;
        }
        public BitmapFromUrl(Context contextl) {
            this.contextl = contextl;
        }
        public ImageView getImageView() {
            return imageView;
        }



        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public Context getContextl() {
            return contextl;
        }

        public void setContextl(Context contextl) {
            this.contextl = contextl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bit;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strings[0]).openConnection();
                httpURLConnection.setConnectTimeout(1800);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                if(httpURLConnection.getResponseCode() == 200){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bit = BitmapFactory.decodeStream(inputStream);
                    return bit;
                }else {
                    Toast.makeText(contextl, "Error: "+httpURLConnection.getResponseCode()+" Message: "+httpURLConnection.getResponseMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                }
            } catch (MalformedURLException e) {
                Toast.makeText(contextl, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            } catch (IOException e) {
                Toast.makeText(contextl, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(imageView == null)
                return;
            if(bitmap!= null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(R.drawable.ic_error_black_24dp);
        }
    }
    public static String getTime(int dua){
        String time= String.format("%02d:%02d",dua/60,dua%60);
        return time;
    };

}
