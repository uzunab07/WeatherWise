package edu.uncc.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.weather.databinding.FragmentCurrentWeatherBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CurrentWeatherFragment extends Fragment {
    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";
    private DataService.City mCity;
    Weather weather;
    FragmentCurrentWeatherBinding binding;



    private final OkHttpClient client = new OkHttpClient();

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(DataService.City city) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (DataService.City) getArguments().getSerializable(ARG_PARAM_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false);
        binding.textViewCityName.setText(mCity.getCity()+", "+mCity.getCountry());
        showCurrentWeather();

        /*
        binding.buttonCheckForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.rootView,WeatherForecastFragment.newInstance(mCity),"WeatherForecastFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
         */
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Weather");

        binding.buttonCheckForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CWlistener.goToForecast(mCity);
            }
        });
    }
    public void showCurrentWeather(){
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather").newBuilder()
                .addQueryParameter("lat",String.valueOf(mCity.getLat()))
                .addQueryParameter("lon",String.valueOf(mCity.getLon()))
                .addQueryParameter("appid","d0f7922578a4d76d5f9737f9c9e610a1")
                .addQueryParameter("units","imperial")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if(response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    Gson gson = new Gson();
                    weather = gson.fromJson(response.body().charStream(),Weather.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.textViewTemp.setText(weather.main.temp+" F");
                            binding.textViewTempMax.setText(weather.main.temp_max+" F");
                            binding.textViewTempMin.setText(weather.main.temp_min+" F");
                            binding.textViewDesc.setText(weather.weather.get(0).description);
                            binding.textViewHumidity.setText(weather.main.humidity+"%");
                            binding.textViewWindSpeed.setText(weather.wind.speed+" miles/hr");
                            binding.textViewWindDegree.setText(weather.wind.deg+" degrees");
                            binding.textViewCloudiness.setText(weather.clouds.all+"%");
                            HttpUrl urlPicIcon = HttpUrl.parse("https://openweathermap.org/img/wn/")
                                    .newBuilder().addPathSegment(weather.weather.get(0).icon+"@2x.png")
                                    .build();
                            String newUrl = urlPicIcon.toString();
                            Picasso.get().load(newUrl).into(binding.imageViewWeatherIcon);
                        }
                    });

                }else{
                    Log.d("demo", "onFailure: "+response.body());
                }
            }
        });

    }


    cwListener CWlistener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        CWlistener = (cwListener) context;
    }

    public interface cwListener {
        void goToForecast(DataService.City mCity);
    }
}