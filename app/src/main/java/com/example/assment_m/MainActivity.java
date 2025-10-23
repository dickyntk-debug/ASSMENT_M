package com.example.assment_m;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    Button btn2;
    ExecutorService fixedThreadPool;

    LinearLayout llm2;

    RecyclerView rv;

    @Override
    protected void onStart() {
        super.onStart();

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);


        if (hour >= 6 && hour < 12) {
            llm2.setBackground(new ColorDrawable(Color.parseColor("#B2D6C68B")));
        } else if (hour >= 12 && hour < 18) {

            llm2.setBackground(new ColorDrawable(Color.parseColor("#FFE39B64")));
        } else {

            llm2.setBackground(new ColorDrawable(Color.parseColor("#FF8A86C8")));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        btn2 = findViewById(R.id.btn2);
        llm2 = findViewById(R.id.llm2);
        fixedThreadPool = Executors.newFixedThreadPool(1);



        getJSON("https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=fnd&lang=tc");

        btn2.setOnClickListener(view->play());


    }


    public void play()
    {

        Intent i = new Intent(MainActivity.this,menuActivity.class);

        startActivity(i);
    }

    public void receiveJSON(String response) {

        ArrayList<ItemViewModel> data =  new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray weatherForecast= jsonResponse.getJSONArray("weatherForecast");

            for(int i = 0; i<7;i++) {

                JSONObject item = weatherForecast.getJSONObject(i);
                JSONObject forecastMaxtemp = item.getJSONObject("forecastMaxtemp");
                int tempMax = forecastMaxtemp.getInt("value");
                JSONObject forecastMintemp = item.getJSONObject("forecastMintemp");
                int tempMin = forecastMintemp.getInt("value");

                String windSpeed = item.getString("forecastWind");
                String forecastWeather = item.getString("forecastWeather");
                String week = item.getString("week");


                data.add(new ItemViewModel( tempMax,tempMin , windSpeed,week,forecastWeather));
            }
                rv.setLayoutManager(new LinearLayoutManager(this));
                rv.setAdapter(new CustomAdapter(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    public void getJSON(String website){
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
                    while((line = reader.readLine())!= null){
                        sb.append(line + "\n");
                    }
                    reader.close();
                    ((HttpsURLConnection)urlConnection).disconnect();

                    runOnUiThread(()->receiveJSON(sb.toString().trim()));

                }catch (Exception e){

                }
            }
        });
    }
}