package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultText;
    public void findWeather(View view)
    {
        //We need to remove the keyboard so the person can look at the result
        InputMethodManager inputMethodManager= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  //gets the keyboard
        inputMethodManager.hideSoftInputFromWindow(cityName.getWindowToken(),0);   //removes the keyboard

        try {
            String encodedCityName= URLEncoder.encode(cityName.getText().toString(),"UTF-8");
            DownloadJSON task= new DownloadJSON();
            task.execute("https://api.openweathermap.org/data/2.5/weather?q="+encodedCityName+"&appid=0e8f283a0a8f5b1f121b82d69ee1ccf2");

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(getApplicationContext(), "We could not find that city :(", Toast.LENGTH_SHORT).show();
        }


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

            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "We could not find that city :(", Toast.LENGTH_SHORT);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Step 1: Converting String to actual JSON Data
            try {
                String message="";
                JSONObject jsonObject = new JSONObject(result);
                //Step 2: Extracting the favourable part
                String weatherinfo = jsonObject.getString("weather");
                String tempAndPressure = jsonObject.getString("main"); //Extracts the weather part
                Log.i("Weather Content", weatherinfo);
                Log.i("Temp", tempAndPressure);
                //Step 3: Looping through the data
                JSONArray jsonArray = new JSONArray(weatherinfo);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    String main="";
                    String desc="";

                    main= jsonPart.getString("main");
                    desc=jsonPart.getString("description");
                    Log.i("Main", jsonPart.getString("main"));
                    Log.i("Description", jsonPart.getString("description"));

                    if(main != "" && desc!= "")
                    {
                        message+= main +" : "+desc+"\r\n";
                    }
                }
                if(message != "")
                {
                    resultText.setText(message);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "We could not find that city :(", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "We could not find that city :(", Toast.LENGTH_SHORT).show();
              //  e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText=(TextView)findViewById(R.id.resultText);
        cityName= (EditText)findViewById(R.id.location);
    }
}
