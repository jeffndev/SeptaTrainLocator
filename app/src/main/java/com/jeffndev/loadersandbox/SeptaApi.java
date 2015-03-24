package com.jeffndev.loadersandbox;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jeffreynewell1 on 3/21/15.
 */
public class SeptaApi extends AsyncTask<String, Void, String>{
    final String LOG_TAG = SeptaApi.class.getSimpleName();
    final String SEPTA_API_BASE_URL = "http://www3.septa.org/hackathon/";

    final String [] stationNames = new String [] {
        "30th Street Station",
        "Suburban Station",
        "Jefferson",
        "Temple U"
    };

    @Override
    protected void onPostExecute(String trainsJson) {
        //super.onPostExecute(aVoid);
        Log.v(LOG_TAG,"On Post Execute got the json");
    }

    @Override
    protected String doInBackground(String... params) {

        String trainsJson = getNextNTrainsAtLocation(5,"Suburban Station");

        return trainsJson;
    }

    String getNextNTrainsAtLocation(int numTrainsToShow, String locationName){
        final String encodingString = "UTF-8";
//        String encodedLocation;
//        try {
//            encodedLocation = URLEncoder.encode(locationName, encodingString);
//        }catch (UnsupportedEncodingException badCoding){
//            Log.d(LOG_TAG,"Bad url encoding (" + encodingString + ") on: " + locationName);
//            return null;
//        }
        String jsonReturned = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            Uri builtUri = Uri.parse(SEPTA_API_BASE_URL).buildUpon()
                    .appendPath("Arrivals")
                    .appendPath(locationName)
                    .appendPath(Integer.toString(numTrainsToShow)).build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String ln;
            StringBuilder sb = new StringBuilder();
            while( (ln = reader.readLine()) != null){
                sb.append(ln).append('\n');
            }
            jsonReturned = sb.toString();
            Log.v(LOG_TAG, jsonReturned);

        }catch(MalformedURLException badUrl){
            Log.d(LOG_TAG, "bad url: ",badUrl);
        }catch(IOException badIo){
            Log.d(LOG_TAG,"bad io: ", badIo);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch (final IOException badClose){
                    Log.e(LOG_TAG, "Error closing url reader stream", badClose);
                }
            }
        }
        return jsonReturned;
    }
}
