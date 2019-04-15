package com.example.group5_inclass5;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class GetImagesData extends AsyncTask<String, Void, String> {

    final String api = "http://dev.theappsdr.com/apis/photos/index.php?keyword=";
    MainActivity currentActivity;


    public GetImagesData(MainActivity currentActivity)
    {
        this.currentActivity = currentActivity;
    }

    public GetImagesData() {

    }


    @Override
    protected String doInBackground(String... params) {
        //Log.d("demo","inGetImagesDataDoIn");
        BufferedReader reader = null;

        try
        {

            URL url = new URL(api+params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            return sb.toString();
        }
        catch (Exception ex)
        {

        }
        finally
        {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Log.d("demo","inGetImagesDataPreExecute");
        currentActivity.progressBar.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //Log.d("demo","inGetImagesDataPostExecute");
        currentActivity.progressBar.setVisibility(View.INVISIBLE);
        //currentActivity.currentResult = s;

        Log.d("demo", "Data retrieved: " + s.toString());

        currentActivity.getImagesFromDictionary(s);


    }

    static public interface IData
    {
        public void getImagesFromDictionary(String result);
    }
}