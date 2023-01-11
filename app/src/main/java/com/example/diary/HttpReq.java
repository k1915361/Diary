package com.example.diary;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.diary.Helper.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpReq {

    public static String loadDogImage(Context context, String urll){
        RequestQueue volleyQueue = Volley.newRequestQueue(context);
        String url = "https://dog.ceo/api/breeds/image/random";
        url = urll;
        String res="";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            (Response.Listener<JSONObject>) response -> {
                String dogImageUrl;
                try {
                    dogImageUrl = response.getString("message");
                    toast(context, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            },
            (Response.ErrorListener) error -> {
                Log.e("HttpReq", "loadDogImage error: ${error.localizedMessage}");
            }
        );
        volleyQueue.add(jsonObjectRequest);
        return jsonObjectRequest.toString();
    }



    public String httpReq(String url) throws IOException {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");
//
//        try {
//            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("id", "12345"));
//            nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//
//        } catch (Exception e) {
//        } catch (IOException e) {
//        }
        return "";
    }
    public String httpReqV1(String url) throws IOException {
//        HttpUriRequest request = new HttpGet( url );
//        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
//        if(httpResponse.getStatusLine().getStatusCode() == 200) return "Failed";
//
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                httpResponse.getEntity().getContent()));
//
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = reader.readLine()) != null) {
//            response.append(inputLine);
//        }
//        reader.close();
        return "";
    }

}
