package com.mooo.ewolvy.killkodi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

class KillKodiAsyncTask extends AsyncTask <KKState, Void, KKState>{
    private Context context;
    private ProgressDialog pDialog;
    private final String LOG_TAG = "KillKodiAsyncTask";

    KillKodiAsyncTask(Context cont){
        context = cont;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog (context);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage(context.getString(R.string.pdialog_message));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(KKState k) {
        super.onPostExecute(k);
        pDialog.dismiss();
        if (k.getIsKilled()) {
            Toast.makeText(context, context.getString(R.string.kodi_killed),
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, context.getString(R.string.kodi_alive),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected KKState doInBackground(KKState... kkStates) {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (kkStates.length < 1 || kkStates[0] == null) {
            return null;
        }            return fetchKillKodiData(kkStates[0].getKodiURL());
    }

    /**
     *
     */
    private KKState fetchKillKodiData(URL requestUrl) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractFeatureFromJson(jsonResponse, requestUrl);
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            String userCredentials = "prueba:prueba";
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), 0);
            urlConnection.setRequestProperty ("Authorization", basicAuth);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private KKState extractFeatureFromJson(String stateJSON, URL url) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(stateJSON)) {
            return null;
        }
        KKState returnState = new KKState(url.getHost(), url.getPort());
        try {
            JSONObject baseJsonResponse = new JSONObject(stateJSON);

            if (baseJsonResponse.length() > 0) {
                // Extract out if is killed
                Boolean isKilled = baseJsonResponse.getBoolean("killed");

                returnState.setIsKilled(isKilled);
                // Create a new {@link Event} object
                return returnState;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return null;
    }
}
