package com.example.assment_m;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;


public class SecondaryAct extends AppCompatActivity  {
    EditText etName;

    TextView tvshowrs;
    Button btnck,btnback;
    ExecutorService fixedThreadPool;

    String name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loctioncheck);

        etName = findViewById(R.id.etName);

        btnck = findViewById(R.id.btnck);

        btnback = findViewById(R.id.btnback);
        tvshowrs = findViewById(R.id.tvshowrs);

        btnck.setOnClickListener(view -> login());
        btnback.setOnClickListener(view -> play());
        fixedThreadPool = Executors.newFixedThreadPool(1);


    }

    private void login() {
        name =etName.getText().toString();
        String wbs2 ="https://api.openweathermap.org/data/2.5/weather?q="+name+"&appid=12049a610c4cc09d2c89dacb1d0d74df";

        getJSON(wbs2);

    }
    public void play()
    {

        Intent i = new Intent(SecondaryAct.this,menuActivity.class);

        startActivity(i);
    }
    public void receiveJSON(String response) {



          try {
              JSONObject jsonObject = new JSONObject(response);

              String description = jsonObject.getJSONArray("weather")
                      .getJSONObject(0)
                      .getString("description");


              int temp = (int) ((jsonObject.getJSONObject("main").getDouble("temp")) - 273.15f);

              int tempMin = (int) ((jsonObject.getJSONObject("main")
                      .getDouble("temp_min")) - 273.15f);


              int tempMax = (int) ((jsonObject.getJSONObject("main")
                      .getDouble("temp_max")) - 273.15f);


              int humidity = jsonObject.getJSONObject("main")
                      .getInt("humidity");

              double speed = jsonObject.getJSONObject("wind")
                      .getDouble("speed");

              String name = jsonObject.getString("name");

              String text = "City Name: " + name +
                      "\nDescription: " + description +
                      "\nTemperature: " + temp + "°C" +
                      "\nMin Temperature: " + tempMin + "°C" +
                      "\nMax Temperature: " + tempMax + "°C" +
                      "\nHumidity: " + humidity +
                      "\nWind Speed: " + speed;





                  tvshowrs.setText(text);


          } catch (JSONException e) {
              e.printStackTrace();

              runOnUiThread(() -> tvshowrs.setText("There is no information, maybe the country is entered incorrectly"));
          }
      }


    public void getJSON(String website) {
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb = new StringBuffer();
                try {
                    URL url = new URL(website);
                    URLConnection urlConnection = url.openConnection();
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
                    httpsURLConnection.setRequestMethod("GET");

                    InputStream is = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    reader.close();
                    ((HttpsURLConnection) urlConnection).disconnect();

                    runOnUiThread(() -> receiveJSON(sb.toString().trim()));

                } catch (Exception e) {
                    e.printStackTrace();

                    runOnUiThread(() -> tvshowrs.setText("There is no information, maybe the country is entered incorrectly"));
                }
            }
        });
    }

}
