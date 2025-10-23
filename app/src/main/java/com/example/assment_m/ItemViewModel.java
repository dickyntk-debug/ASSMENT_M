package com.example.assment_m;

public class ItemViewModel {


    private int temp_max;
    int temp_min;
    private String  speed;
    String week;

    String forecastWeather;
    public ItemViewModel( int temp_max,int temp_min, String speed,String week,String forecastWeather ) {

        this.temp_max=temp_max;
        this.temp_min=temp_min;
        this.speed=speed;
        this.week=week;
        this.forecastWeather = forecastWeather;
    }


    public String getSpeed() {   return speed; }
    public int getTempMax () {   return temp_max; }
    public  int getTempMin(){return temp_min;}
    public String getWeek() {   return week; }
    public String getFW() {   return forecastWeather; }
    public void setSpeed(String  speed) {   this.speed = speed;  }

    public void setTempMax (int temp_max) {   this.temp_max = temp_max;  }
    public void setTempMin (int temp_min) {   this.temp_min = temp_min;  }
    public void setWeek(String week) {   this.week = week;  }

    public  void setFW(String forecastWeather){this.forecastWeather = forecastWeather;}
}
