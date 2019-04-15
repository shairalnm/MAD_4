
package com.example.group5_inclass5;


import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GetImagesData.IData {


    TextView tvKeyword;
    Button btnGo;
    ImageView ivPictures, ivPrev, ivNext;
    AlertDialog adKeywords;
    ProgressBar progressBar;
    ArrayList<String> imageURLs;
    int currentPosition;
    String[] items=null;
    String values=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvKeyword = (TextView) findViewById(R.id.tvKeyword);
        btnGo = (Button) findViewById(R.id.btnGo);
        ivPictures = (ImageView) findViewById(R.id.ivPictures);
        ivPrev = (ImageView) findViewById(R.id.ivPrev);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        progressBar = (ProgressBar) findViewById(R.id.progressImages);


        if(isConnected())
        {
            new GetKeywordAsyncTask().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
            btnGo.setEnabled(true);

        }


        ivPrev.setEnabled(false);
        ivNext.setEnabled(false);

        setEventHandlers();
        configureProgressDialog();
        configurePictureProgressDialog();
    }

    private void setEventHandlers()
    {
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPrev.setEnabled(true);
                ivNext.setEnabled(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //Log.d("demo","onclick"+items[i]);
                        tvKeyword.setText(items[i]);
                        values=items[i];
                        Log.d("demo",values);
                        progressBar.setVisibility(View.VISIBLE);
                        new GetImagesData(MainActivity.this).execute(items[i].toString());

                    }
                });
                builder.create().show();

            }
           });


        ivPrev.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Log.d("demo","prev clicked");
                if (currentPosition == 0)
                {
                    currentPosition = (imageURLs.size()-1);
                }
                else
                {
                    currentPosition = currentPosition - 1;
                }
                //pbLoadingPictures.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("demo","next clicked");

                if (currentPosition<(imageURLs.size()-1))
                {
                    currentPosition = currentPosition + 1;
                }
                else if (currentPosition == (imageURLs.size()-1))
                {
                    currentPosition = 0;
                }
                //pbLoadingPictures.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));

            }
        });
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private void configureProgressDialog()
    {

        //pbLoading.setVisibility(View.VISIBLE);
    }

    private void configurePictureProgressDialog()
    {
        //pbLoadingPictures.setVisibility(View.VISIBLE);
    }


    @Override
    public void getImagesFromDictionary(String result) {

        imageURLs = new ArrayList<String>();
        imageURLs.addAll(Arrays.asList(result.split(".jpg")));
        Log.d("demo","Image urls"+imageURLs.toString());

        imageURLs.remove(0);
        for(int i=0;i<imageURLs.size();i++)
        {
            imageURLs.set(i,imageURLs.get(i).concat(".jpg")) ;
            Log.d("demo","Image new url"+imageURLs.get(i));
        }
        currentPosition = 0;
        Log.d("demo","Image new url"+imageURLs.toString());
        if (!isConnected())
        {
            Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }


        if (imageURLs.size() == 0)
        {
            ivPrev.setEnabled(false);
            ivNext.setEnabled(false);
            ivPictures.setImageBitmap(null);
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "No images found.", Toast.LENGTH_SHORT).show();
        }
        else if (imageURLs.size() == 1)
        {
            ivPrev.setEnabled(false);
            ivNext.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));
        }
        else
        {
            ivPrev.setEnabled(true);
            ivNext.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));

        }

    }


    class GetKeywordAsyncTask extends AsyncTask<String, Void, String[]>
    {


        @Override
        protected String[] doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;String result = null;
            String[] parts= null;


            try {
                URL url;
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
                    Log.d("demo","keywords result" +result);
                    parts = result.split(";");
                    Log.d("demo","Keywords"+ Arrays.toString(parts));


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return (parts);


        }


        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            items=s;


        }


    }

}
