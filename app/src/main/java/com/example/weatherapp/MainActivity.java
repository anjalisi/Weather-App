package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    public void findWeather(View view)
    {

        DownloadJSON task= new DownloadJSON();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q="+cityName.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");

        Log.i("City Name", cityName.getText().toString());
    }
    public class DownloadJSON extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            String result= "";
            URL url;
            HttpURLConnection connection= null;
            try
            {
                url= new URL(urls[0]);
                connection= (HttpURLConnection)url.openConnection();
                InputStream is= connection.getInputStream();
                InputStreamReader reader= new InputStreamReader(is);
                int data= reader.read();

                while(data != -1)
                {
                    char current= (char)data;
                    result += current;
                    data= reader.read();
                }
                return result;
            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Step 1: Converting String to actual JSON Data
            try {
                JSONObject jsonObject = new JSONObject(result);
                //Step 2: Extracting the favourable part
                String weatherinfo = jsonObject.getString("weather");    //Extracts the weather part
                Log.i("Weather Content", weatherinfo);

                //Step 3: Looping through the data
                JSONArray jsonArray = new JSONArray(weatherinfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    Log.i("Main", jsonPart.getString("main"));
                    Log.i("Description", jsonPart.getString("description"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName= (EditText)findViewById(R.id.location);
    }
}
