package valhalla.asgardremote;

/**
 * Created by FCapo on 03/03/2017.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HTTPDataHandler {

    static String v_rawData = null;
    private static final String TAG = "HTTP";
    public HTTPDataHandler(){
    }

    public String GetHTTPData(String p_URL){

        try{
            URL v_url = new URL(p_URL);
            Log.i(TAG,"heloo2");
            HttpURLConnection v_connection = (HttpURLConnection) v_url.openConnection();
            Log.i(TAG,"heloo3");
            // Check the connection status
            Log.i(TAG,"code: "+v_connection.getResponseCode());
            Log.i(TAG,"especial");
            if(v_connection.getResponseCode() == 200)// if response code = 200 ok
            {
                Log.i(TAG,"heloo4");
                InputStream v_resultFromRequest = new BufferedInputStream(v_connection.getInputStream());
                Log.i(TAG,"heloo5");
                // Read the BufferedInputStream
                BufferedReader v_buffer = new BufferedReader(new InputStreamReader(v_resultFromRequest));
                StringBuilder v_stringBuilder = new StringBuilder();
                String v_bufferToken;
                while ((v_bufferToken = v_buffer.readLine()) != null) {
                    v_stringBuilder.append(v_bufferToken);
                }
                v_rawData = v_stringBuilder.toString();
                // Disconnect the HttpURLConnection
                v_connection.disconnect();
            }
            else
            {
                // Do something when i get erros i'll think about it
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally {

        }
        // this is the request result as a big raw json ;)
        return v_rawData;
    }
}