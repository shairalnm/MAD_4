package com.example.group5_inclass5;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.net.HttpURLConnection;
import java.net.URL;



public class GetImages extends AsyncTask<String, Void, Bitmap> {

    MainActivity currentActivity;


    public GetImages(MainActivity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public GetImages() {

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        //Log.d("demo","inGetImagesDoIn");
        try
        {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            Bitmap image = BitmapFactory.decodeStream(con.getInputStream());
            return image;
        }
        catch (Exception ex)
        {

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Log.d("demo","inGetImagesPreExecute");
        //currentActivity.pbLoadingPictures.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        //Log.d("demo","inGetImagesPostExecute");
        //currentActivity.pbLoadingPictures.setVisibility(View.INVISIBLE);
        currentActivity.ivPictures.setImageBitmap(bitmap);
        currentActivity.progressBar.setVisibility(View.INVISIBLE);

    }

}
