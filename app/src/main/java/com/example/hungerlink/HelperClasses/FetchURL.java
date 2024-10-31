//package com.example.hungerlink;
//
//import android.os.AsyncTask;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class FetchURL extends AsyncTask<String, Void, String> {
//    private final ResponseHandler handler;
//
//    public FetchURL(ResponseHandler handler) {
//        this.handler = handler;
//    }
//
//    @Override
//    protected String doInBackground(String... strings) {
//        String data = "";
//        try {
//            URL url = new URL(strings[0]);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuffer sb = new StringBuffer();
//            String line;
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            data = sb.toString();
//            br.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return data;
//    }
//
//    @Override
//    protected void onPostExecute(String s) {
//        handler.onComplete(s);
//    }
//
//    public interface ResponseHandler {
//        void onComplete(String data);
//    }
//}
//
//


package com.example.hungerlink.HelperClasses;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchURL extends AsyncTask<String, Void, String> {
    private final ResponseHandler handler;

    public FetchURL(ResponseHandler handler) {
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        HttpURLConnection connection = null;
        BufferedReader br = null;

        try {
            // Set up the URL and connection
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);  // 10 seconds timeout for connection
            connection.setReadTimeout(10000);     // 10 seconds timeout for reading

            // Read the response
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the BufferedReader and disconnect the connection
            try {
                if (br != null) br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        handler.onComplete(s);  // Pass data to the handler on completion
    }

    // Interface for handling the response
    public interface ResponseHandler {
        void onComplete(String data);
    }
}
