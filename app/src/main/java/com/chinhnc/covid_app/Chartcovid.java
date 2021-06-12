package com.chinhnc.covid_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chinhnc.covid_app.API.ApiService;
import com.chinhnc.covid_app.Modul.Covid;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chartcovid extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartcovid);
        Intent intent = getIntent();
        String check = intent.getStringExtra("check_input");
        if (check.equals("0") == true) {
            callApi(0);
        } else {
            callApi(1);
        }

        Toast.makeText(Chartcovid.this, "Click vào màn hình để lấy dữ liệu", Toast.LENGTH_LONG).show();
//
    }

    private void callApi(int i) {
        if (i == 0) {
            ApiService.apiService.convertapitovn("").enqueue(new Callback<Covid>() {
                @Override
                public void onResponse(Call<Covid> call, Response<Covid> response) {
                    //ánh xạ
                    Covid covid = response.body();
                    if (covid != null) {
                        int cn = covid.getCases();
                        int dt = covid.getActive();
                        int hp = covid.getRecovered();
                        int tv = covid.getDeaths();
                        chart(cn, tv, dt, hp);

                    }
                }

                @Override
                public void onFailure(Call<Covid> call, Throwable t) {

                }
            });
        } else {
            ApiService.apiService.convertapitoall("").enqueue(new Callback<Covid>() {
                @Override
                public void onResponse(Call<Covid> call, Response<Covid> response) {
                    Covid covid = response.body();
                    if (covid != null) {
                        int cn = covid.getCases();
                        int dt = covid.getActive();
                        int hp = covid.getRecovered();
                        int tv = covid.getDeaths();
                        chart(cn, tv, dt, hp);

                    }
                }

                @Override
                public void onFailure(Call<Covid> call, Throwable t) {

                }
            });
        }


    }

    private void chart(int cn, int tv, int dt, int hp) {
        PieChart pieChart = (PieChart) findViewById(R.id.chart);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        //  pieEntries.add(new PieEntry(2000, "ca nhiễm"));
        pieEntries.add(new PieEntry(hp, "Hồi phục"));
        pieEntries.add(new PieEntry(dt, "Điều trị"));
        pieEntries.add(new PieEntry(tv, "Tử vong"));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Tổng: " + cn);
        pieChart.animate();
    }


}