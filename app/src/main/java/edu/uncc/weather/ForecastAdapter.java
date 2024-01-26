package edu.uncc.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

public class ForecastAdapter extends ArrayAdapter<Forecast> {
    public ForecastAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.forecast_row_item, parent, false);
        }

        Forecast forecast = getItem(position);

        Log.d("demo", "Forecast Item: "+forecast);

        TextView dateTime, temp, tempMin, tempMax, humidity, desc;
        ImageView icon;

        dateTime = convertView.findViewById(R.id.textViewDateTime);
        temp = convertView.findViewById(R.id.textViewTemp);
        tempMin = convertView.findViewById(R.id.textViewTempMin);
        tempMax = convertView.findViewById(R.id.textViewTempMax);
        humidity = convertView.findViewById(R.id.textViewHumidity);
        desc = convertView.findViewById(R.id.textViewDesc);
        icon = convertView.findViewById(R.id.imageViewWeatherIcon);

        dateTime.setText(forecast.dt_txt);
        temp.setText(forecast.main.temp+" F");
        tempMin.setText(forecast.main.temp_min+" F");
        tempMax.setText(forecast.main.temp_max+" F");
        humidity.setText("Humidity: "+forecast.main.humidity+"%");
        desc.setText(forecast.weather.get(0).description);



        HttpUrl urlPicIcon = HttpUrl.parse("https://openweathermap.org/img/wn/")
                .newBuilder().addPathSegment(forecast.weather.get(0).icon+"@2x.png")
                        .build();

        String newUrl = urlPicIcon.toString();
        Picasso.get().load(newUrl).into(icon);
        return convertView;
    }
}
