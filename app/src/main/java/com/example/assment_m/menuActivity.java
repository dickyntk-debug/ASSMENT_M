package com.example.assment_m;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;





public class menuActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    TextView tvL,tvL2,tvL3,tvTime,ltvL2,ltvtile,ltvL3,ltvLUV;

    TextView tvCM,tvC1,tvC2;
    ImageView im1;
    FrameLayout container;
    Button btn,btns,btnload,btnckto;

    LinearLayout llm;

    private LocationManager lm;

    boolean isSave2 = false;
    boolean gpsStatus;

    boolean isSave ;





    ExecutorService fixedThreadPool;
    ExecutorService fixedThreadPool2;
    ExecutorService fixedThreadPool3;
    public double latitude;
    public double longitude;

    private Handler handler;

    String formattedAverageTemperature;
    String uv ;

    String formattedTime;

    String winfo ;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        super.onStart();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);


        if (hour >= 6 && hour < 12) {
            llm.setBackground(ContextCompat.getDrawable(this, R.drawable.morning2));
            llm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#45FFFFFF")));
            TextView[] textViews = new TextView[]
                    {tvCM, tvL,tvTime,tvC1,tvL2,tvC2,tvL3,ltvtile,ltvL2,ltvL3,ltvLUV};

            int color = Color.parseColor("#FF2B2D30");

            for (TextView textView : textViews) {
                textView.setTextColor(ColorStateList.valueOf(color));
            }
        } else if (hour >= 12 && hour < 18) {

            llm.setBackground(ContextCompat.getDrawable(this, R.drawable.noon));
            llm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#45FFFFFF")));

            TextView[] textViews = new TextView[]
                    {tvCM, tvL,tvTime,tvC1,tvL2,tvC2,tvL3,ltvtile,ltvL2,ltvL3,ltvLUV};

            int color = Color.parseColor("#FF2B2D30");

            for (TextView textView : textViews) {
                textView.setTextColor(ColorStateList.valueOf(color));
            }
        } else {

            llm.setBackground(ContextCompat.getDrawable(this, R.drawable.night));
            //"#45FFFFFF"
            llm.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#66021013")));

            TextView[] textViews = new TextView[]
                    {tvCM, tvL,tvTime,tvC1,tvL2,tvC2,tvL3,ltvtile,ltvL2,ltvL3,ltvLUV};

            int color = Color.parseColor("#FFFFFF");

            for (TextView textView : textViews) {
                textView.setTextColor(ColorStateList.valueOf(color));
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        isSave = false;


        tvL = findViewById(R.id.tvL);
        tvL2 = findViewById(R.id.tvL2);
        tvL3 = findViewById(R.id.tvL3);
        ltvL2 = findViewById(R.id.ltvL2);
        ltvtile = findViewById(R.id.ltvtile);
        ltvL3 = findViewById(R.id.ltvL3);
        ltvLUV = findViewById(R.id.ltvLUV);


        im1= findViewById(R.id.im1);

        container = findViewById(R.id.container);

        container.setOnClickListener(view->onClick(view));
        btn = findViewById(R.id.btn);
        btns = findViewById(R.id.btns);
        btnload = findViewById(R.id.btnload);
        btnckto = findViewById(R.id.btnckto);

        tvTime = findViewById(R.id.tvTime);

        tvCM =findViewById(R.id.tvCM);
        tvC1 = findViewById(R.id.tvC1);
        tvC2 = findViewById(R.id.tvC2);

        llm = findViewById(R.id.llm);
        btn.setOnClickListener(view->play());
        btns.setOnClickListener(view->input());
        btnload.setOnClickListener(view->show());
        btnckto.setOnClickListener(view->play2());
        handler = new Handler();

        Runnable updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                updateCurrentTime();
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(updateTimeRunnable); // 開始更新時間

        fixedThreadPool = Executors.newFixedThreadPool(1);
        fixedThreadPool2 = Executors.newFixedThreadPool(1);
        fixedThreadPool3 = Executors.newFixedThreadPool(1);

        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}
                    , 999);
        }
        gpsStatus = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5f, this);




        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String wbs3 ="https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=flw&lang=tc";
        getJSON3(wbs3);

    }

    @Override
    public void onClick(View v) {

        createExitDialogBox();
    }
    public void createExitDialogBox() {


        AlertDialog.Builder adExit = new AlertDialog.Builder(this);
        adExit.setTitle("");
        adExit.setMessage(winfo);
        adExit.setCancelable(false);

        adExit.setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });



        adExit.create();
        adExit.show();
    }


    public void info()
    {


    }

    public void input() {

        editor.putString("c", formattedAverageTemperature);
        editor.putString("t", formattedTime);
        editor.putString("u", uv);
        editor.apply();
        isSave = true;
        //String username = sharedPreferences.getString("c", "");
    }
    public void show(){




        if(isSave){
        String username = sharedPreferences.getString("c", "");
        String time = sharedPreferences.getString("t", "");
        String uvValue = sharedPreferences.getString("u", "");


        ltvtile.setText("平均溫度: ");
        ltvL2.setText(username+"°C");
        ltvL3.setText("上次儲存時間:\n"+time);
        ltvLUV.setText("紫光線指數: "+uvValue);

        isSave2 =true;

        }
        if(!isSave2)
        {
            ltvtile.setText("沒有任何天氣記錄");
        }


        isSave= false;
    }

    private void updateCurrentTime() {
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        String weekday = getWeekday(dayOfWeek);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd "+weekday+" HH:mm:ss");


        formattedTime = sdf.format(currentTime);



        tvTime.setText(formattedTime);
    }
    private static String getWeekday(int dayOfWeek) {
        String[] weekdays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return weekdays[dayOfWeek - 1];
    }
    public void play()
    {
        Intent i = new Intent(menuActivity.this,MainActivity.class);

        startActivity(i);
    }
    public void play2()
    {
        Intent i = new Intent(menuActivity.this,SecondaryAct .class);

        startActivity(i);
    }
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // 从安全配置获取API密钥

        String apiKey = "";

        String wbs = "https://api.openweathermap.org/geo/1.0/reverse?lat=" +
                Double.toString(latitude) + "&lon=" +
                Double.toString(longitude) + "&limit=5&appid=" + apiKey;

        getJSON(wbs);

        String wbs2 = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=rhrread&lang=tc";
        getJSON2(wbs2);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void receiveJSON(String response) {


        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String state = jsonObject.getString("state");
            JSONObject localNames = jsonObject.getJSONObject("local_names");
            String value = localNames.getString("zh");
            tvL.setText("當前位置:"+state+","+value);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void receiveJSON2(String response) {


        try {

            JSONObject jsonObject = new JSONObject(response);
            String icon = jsonObject.getJSONArray("icon").getString(0);




            String imageUrl ="https://www.hko.gov.hk/images/HKOWxIconOutline/pic"+icon+".png";
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(this)
                    .load(imageUrl)
                    .apply(options)
                    .into(im1);

            JSONArray temperatureData = jsonObject.getJSONObject("temperature").getJSONArray("data");

            double sum = 0;
            for (int i = 0; i < temperatureData.length(); i++) {
                double value = temperatureData.getJSONObject(i).getDouble("value");
                sum += value;
            }

            double averageTemperature = sum / temperatureData.length();
            DecimalFormat decimalFormat = new DecimalFormat("0"); // 格式化為沒有小數位
            formattedAverageTemperature = decimalFormat.format(averageTemperature);
            tvL2.setText(formattedAverageTemperature+"°C");

            JSONObject uvindexObject = jsonObject.getJSONObject("uvindex");
            JSONArray uvindexData = uvindexObject.getJSONArray("data");
            if (uvindexData.length() > 0) {
                JSONObject uvindexDataObject = uvindexData.getJSONObject(0);
                int uvindexValue = uvindexDataObject.getInt("value");
                String uvindexDesc = uvindexDataObject.getString("desc");
                uv =uvindexValue+",強度:"+uvindexDesc;
                tvL3.setText(uv);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void receiveJSON3(String response) {


        try {
            JSONObject jsonObject = new JSONObject(response);
            String generalSituation = jsonObject.getString("generalSituation");
            String tcInfo = jsonObject.getString("tcInfo");
            String fireDangerWarning = jsonObject.getString("fireDangerWarning");
            String forecastPeriod = jsonObject.getString("forecastPeriod");
            String forecastDesc= jsonObject.getString("forecastDesc");
            String outlook = jsonObject.getString("outlook");

            winfo =generalSituation+"\n"+
                    tcInfo+"\n"+
                    fireDangerWarning+"\n"+
                    forecastPeriod+"\n"+"\n"+
                    forecastDesc+"\n"+
                    outlook;


        } catch (JSONException e) {
            e.printStackTrace();
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

                }
            }
        });
    }

    public void getJSON2(String website) {
        fixedThreadPool2.execute(new Runnable() {
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

                    runOnUiThread(() -> receiveJSON2(sb.toString().trim()));

                } catch (Exception e) {

                }
            }
        });
    }

    public void getJSON3(String website) {
        fixedThreadPool2.execute(new Runnable() {
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

                    runOnUiThread(() -> receiveJSON3(sb.toString().trim()));

                } catch (Exception e) {

                }
            }
        });
    }
}



