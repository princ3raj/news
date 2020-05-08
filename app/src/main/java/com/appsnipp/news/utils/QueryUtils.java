package com.appsnipp.news.utils;


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.appsnipp.news.model.NewsDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.BitmapFactory.decodeStream;

public final class QueryUtils {
    private static final String TAG = "QueryUtils";

    private QueryUtils() {
    }

    private static URL createUrl(String url) {

        URL urlObject = null;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException e) {
            //
        }

        return urlObject;
    }

    private static String makeHttpRequest(URL urlObject) throws IOException {

        String jsonResponse = "";
        if (urlObject == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection=null;
        InputStream inputStream=null;

        try {
            httpURLConnection=(HttpURLConnection) urlObject.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode()==200){
                inputStream=(InputStream) httpURLConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            else {
                //
            }
        } catch (IOException e) {
            //
        }finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static ArrayList<NewsDetails> extractDataFromJsonResponse(String jsonResponse){
        ArrayList<NewsDetails> news=new ArrayList<>();
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }

        try {
            JSONObject baseJSONObject=new JSONObject(jsonResponse);
            JSONArray articlesArray=baseJSONObject.getJSONArray("articles");
            for(int i=1;i<articlesArray.length();i++){
                JSONObject currentArticle=articlesArray.getJSONObject(i);
                JSONObject sourceObject=currentArticle.getJSONObject("source");
                String sourceName=sourceObject.getString("name");
                String title=currentArticle.getString("title");
                String description=currentArticle.getString("description");
                String urlOfImage=currentArticle.getString("urlToImage");
//                Bitmap bitmap=fetchThumbnail(urlOfImage);
                String url=currentArticle.getString("url");
                String publishedDate=currentArticle.getString("publishedAt");

                NewsDetails currentNewsDetails=new NewsDetails(title,description,sourceName,publishedDate,urlOfImage,url);
                news.add(currentNewsDetails);
            }
        } catch (JSONException e) {
            //
        }

        return news;
    }


    public static ArrayList<NewsDetails> fetchNewsData(String requestUrl){
        URL url=createUrl(requestUrl);
        String jsonResponse=null;
        try {
            jsonResponse=makeHttpRequest(url);
        } catch (IOException e) {
            //
        }
        ArrayList<NewsDetails> news=extractDataFromJsonResponse(jsonResponse);

        return news;
    }




    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String line=bufferedReader.readLine();
            while (line!=null){
                output.append(line);
                line=bufferedReader.readLine();
            }
        }

        return output.toString();

    }

    //Method that makes HTTP request and returns a Bitmap as the response
    private static Bitmap makeHttpRequestForBitmap(URL url) throws IOException {
        Bitmap bitmapResponse=null;

        //If the URL is null, then return early.
        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                bitmapResponse = decodeStream(inputStream);

            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return bitmapResponse;
    }

    /*
     * For SmallThumbnail Loading
     */
    public static Bitmap fetchThumbnail(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        Bitmap smallThumbnail = null;
        try {
            smallThumbnail = makeHttpRequestForBitmap(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }
        Log.v("SmallThumbnail", String.valueOf(smallThumbnail));
        return smallThumbnail;
    }
}