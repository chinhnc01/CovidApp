package com.chinhnc.covid_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.kit.awareness.Awareness;
import com.huawei.hms.kit.awareness.capture.WeatherStatusResponse;
import com.huawei.hms.kit.awareness.status.WeatherStatus;
import com.huawei.hms.kit.awareness.status.weather.Situation;
import com.huawei.hms.kit.awareness.status.weather.WeatherSituation;
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "weather";
    TextView textViewtemp, textViewweatherid, textViewcity, textViewwind, textViewhum;
    ImageView imageViewwtid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //full màn
        // full màn hình
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //clock
        CustomAnalogClock customAnalogClock = (CustomAnalogClock) findViewById(R.id.analog_clock);
        customAnalogClock.setAutoUpdate(true);
        customAnalogClock.init(WeatherActivity.this, R.drawable.default_face, R.drawable.default_hour_hand, R.drawable.default_minute_hand, 0, false, false);

        anhxa();

        GPScheck();

        changeGPS();

        Awareness.getCaptureClient(this).getWeatherByDevice()
                // Callback listener for execution success.
                .addOnSuccessListener(new OnSuccessListener<WeatherStatusResponse>() {

                    @Override
                    public void onSuccess(WeatherStatusResponse weatherStatusResponse) {
                        WeatherStatus weatherStatus = weatherStatusResponse.getWeatherStatus();
                        WeatherSituation weatherSituation = weatherStatus.getWeatherSituation();
                        Situation situation = weatherSituation.getSituation();
                        // For more weather information, please refer to the API Reference of Awareness Kit.
                        String weatherInfoStr = "City:" + weatherSituation.getCity().getName() + "\n" +
                                "Weather id is " + situation.getWeatherId() + "\n" +
                                "CN Weather id is " + situation.getCnWeatherId() + "\n" +
                                "Temperature is " + situation.getTemperatureC() + "℃" +
                                "," + situation.getTemperatureF() + "℉" + "\n" +
                                "Wind speed is " + situation.getWindSpeed() + "km/h" + "\n" +
                                "Wind direction is " + situation.getWindDir() + "\n" +
                                "Humidity is " + situation.getHumidity() + "%";
                        Log.i(TAG, weatherInfoStr);


                        // xuwr lis dl
                        Setimageweather(getidweather(situation.getWeatherId()));

                        String city = "City: " + weatherSituation.getCity().getName();
                        String hum = "Humidity: " + situation.getHumidity() + "%";
                        String wind = "Wind speed: " + situation.getWindSpeed() + "km/h";
                        String temp = situation.getTemperatureC() + "°";
                        textViewcity.setText(city);
                        textViewhum.setText(hum);
                        textViewwind.setText(wind);
                        textViewtemp.setText(temp);
                    }
                })
                // Callback listener for execution failure.
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "get weather failed");
                    }
                });
    }

    private void anhxa(){
        textViewtemp = findViewById(R.id.textViewtemp);
        textViewweatherid =findViewById(R.id.textViewweatherid);
        textViewcity =findViewById(R.id.textViewcity);
        textViewwind =findViewById(R.id.textViewwind);
        textViewhum =findViewById(R.id.textViewhum);
        imageViewwtid =findViewById(R.id.imageViewwtid);
    }
    //check quyen vi tri
    private void GPScheck(){
        int permission_internet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permission_lc1= ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permission_lc2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission_internet != PackageManager.PERMISSION_GRANTED
                || permission_lc1 != PackageManager.PERMISSION_GRANTED
                || permission_lc2 != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }
    }

    // bắt buộc bật vị trí
    private void changeGPS(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            // khoi dong lại intent
            resetintent();

        }
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    // reset intent
    private void resetintent(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WeatherActivity.this.recreate();
            }
        },3000);

    }
    //set hình ảnh:
    private  void Setimageweather(int i){
        // variable: Một biến để kiểm tra.
        switch ( i ) {
            case  1:
                // nắng ...
                textViewweatherid.setText("Trời nắng");
                imageViewwtid.setImageResource(R.drawable.sun_50px);
                break;
            case  2:
                // mây ngắt quãng, nắng mờ...
                textViewweatherid.setText("Có mây");
                imageViewwtid.setImageResource(R.drawable.partly_cloudy_day_50px);
                break;
            case  3:
                // nhieu mây ...
                textViewweatherid.setText("Nhiều mây");
                imageViewwtid.setImageResource(R.drawable.windy_weather_50px);
                break;
            case  4:
                // sương ...
                textViewweatherid.setText("Sương");
                imageViewwtid.setImageResource(R.drawable.dust_50px);
                break;
            case  5:
                // mưa ...
                textViewweatherid.setText("Có mưa");
                imageViewwtid.setImageResource(R.drawable.rain_50px);
                break;
            case  6:
                // dông ...
                textViewweatherid.setText("Có dông");
                imageViewwtid.setImageResource(R.drawable.cloud_lightning_50px);
                break;
            case  7:
                // có tuyết ...
                textViewweatherid.setText("Có tuyết");
                imageViewwtid.setImageResource(R.drawable.partly_cloudy_day_50px);
                break;
            case  8:
                // đóng băng ...
                textViewweatherid.setText("Băng");
                imageViewwtid.setImageResource(R.drawable.icy_50px);
                break;
            case  9:
                // mưa và tuyết ...
                textViewweatherid.setText("Có mưa tuyết");
                imageViewwtid.setImageResource(R.drawable.sleet_50px);
                break;
            default:
                // Làm gì đó tại đây ...
        }
    }
    //getidweather
    private int getidweather(int i){
        int id = 0;
        if(i ==1 || i==2 ||i==3 || i==30 || i==33 ){
            id = 1;
        } else  if(i ==4 || i==5 ||i==6 || i==34 || i==37){
            id = 2;
        } else if(i ==7 || i==35 ||i==36 || i==38 ){
            id=3;
        } else if(i ==8 || i==11 ||i==43 || i==44 ){
            id=4;
        }else if(i ==12 || i==13 ||i==14 || i==18 || i==39 || i==40 ){
            id=5;
        } else if(i ==15 || i==16 ||i==17 || i==41 || i==42 ) {
            id = 6;
        }else if(i ==19 || i==20 ||i==21 || i==22 || i==23 || i==31 ) {
            id = 7;
        }else if(i ==24 || i==25 ||i==26  ) {
            id = 8;
        }else if(i ==29 ) {
            id = 9;
        }

        return id;
    }
}