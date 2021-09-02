package com.chinhnc.covid_app.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinhnc.covid_app.R;

import java.util.ArrayList;

public class Adapterweather24 extends ArrayAdapter<Weather24> {

    public Adapterweather24(Context context, int resource, ArrayList<Weather24> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_weather24, null);
        }

        Weather24 p = getItem(position);
        if (p != null) {
            TextView textView_time = (TextView) view.findViewById(R.id.textView_time);
            TextView textView_day_night = (TextView) view.findViewById(R.id.textView_day_night);
            TextView textView_temp= (TextView) view.findViewById(R.id.textView_temp);
            TextView textView_rain= (TextView) view.findViewById(R.id.textView_rain);
            ImageView imageday_night = (ImageView) view.findViewById(R.id.imageday_night);

            textView_time.setText(p.time);
            textView_day_night.setText(p.day_night);
            textView_temp.setText(p.temp);
            textView_rain.setText(p.rain);
            imageday_night.setImageResource(p.urlimage);

        }

        return view;
    }
}
