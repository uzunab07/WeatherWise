package edu.uncc.weather;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.uncc.weather.databinding.ForecastColItemBinding;
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

    ArrayList<Forecast> forecasts = new ArrayList<>();

    ForeCastAdapterRecycler adapterRecycler;


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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Weather");


        binding.textViewCityName.setText(mCity.getCity());
        showCurrentWeather();

        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapterRecycler = new ForeCastAdapterRecycler();
        binding.recyclerview.setAdapter(adapterRecycler);

        getForecast();
    }

    public void showCurrentWeather() {
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/weather").newBuilder()
                .addQueryParameter("lat", String.valueOf(mCity.getLat()))
                .addQueryParameter("lon", String.valueOf(mCity.getLon()))
                .addQueryParameter("appid", "d0f7922578a4d76d5f9737f9c9e610a1")
                .addQueryParameter("units", "metric")
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

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    Gson gson = new Gson();
                    weather = gson.fromJson(response.body().charStream(), Weather.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Data Recieved", Toast.LENGTH_SHORT).show();
                            binding.currentTemp.setText(weather.main.temp_max + "°");
                            binding.weatherDesc.setText(weather.weather.get(0).description);
                            binding.tempH.setText("H: " + weather.main.temp_max+"°");
                            binding.tempL.setText(" L: " + weather.main.temp_min+"°");
                            HttpUrl urlPicIcon = HttpUrl.parse("https://openweathermap.org/img/wn/")
                                    .newBuilder().addPathSegment(weather.weather.get(0).icon + "@2x.png")
                                    .build();
                            String newUrl = urlPicIcon.toString();
                            Picasso.get().load(newUrl).into(binding.imageViewWeatherIcon);
                        }
                    });

                } else {
                    Log.d("demo", "onFailure: " + response.body());
                }
            }
        });

    }





    //    Forecasting part
    public void getForecast() {
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/forecast").newBuilder()
                .addQueryParameter("lat", String.valueOf(mCity.getLat()))
                .addQueryParameter("lon", String.valueOf(mCity.getLon()))
                .addQueryParameter("appid", "d0f7922578a4d76d5f9737f9c9e610a1")
                .addQueryParameter("units", "metric")
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

                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    Gson gson = new Gson();
                    ForecastResponse list = gson.fromJson(response.body().charStream(), ForecastResponse.class);

                    forecasts = list.list;


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapterRecycler.notifyDataSetChanged();
                        }
                    });
                } else {
                    Log.d("demo", "onFailure: " + response.body());
                }
            }
        });

    }

    class ForeCastAdapterRecycler extends RecyclerView.Adapter<ForeCastAdapterRecycler.AdapterViewHolder> {


        @NonNull
        @Override
        public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ForecastColItemBinding forecastColItemBinding = ForecastColItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ForeCastAdapterRecycler.AdapterViewHolder(forecastColItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
            try {
                holder.setupUi(forecasts.get(position));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getItemCount() {
            return forecasts.size();
        }

        public class AdapterViewHolder extends RecyclerView.ViewHolder {
            ForecastColItemBinding colItemBinding;

            Forecast forecast;


            public void setupUi(Forecast item) throws ParseException {

                this.forecast = item;

                colItemBinding.degree.setText(forecast.main.temp+"°");
                colItemBinding.humidity.setText(forecast.main.humidity+"%");
                colItemBinding.forecastL.setText(forecast.main.temp_min+"°");
                colItemBinding.forecastH.setText(forecast.main.temp_max+"°");
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                Date date = dt.parse(forecast.dt_txt);
                String time = timeFormat.format(date);
                colItemBinding.time.setText(time);

                HttpUrl urlPicIcon = HttpUrl.parse("https://openweathermap.org/img/wn/")
                        .newBuilder().addPathSegment(forecast.weather.get(0).icon + "@2x.png")
                        .build();
                String forecastUrl = urlPicIcon.toString();

                Log.d("TAGp", "setupUi: "+forecast.weather.get(0).icon);

                Picasso.get().load(forecastUrl).into(colItemBinding.forecastIcon);

            }

            public AdapterViewHolder(@NonNull ForecastColItemBinding forecastColItemBinding) {
                super(forecastColItemBinding.getRoot());
                this.colItemBinding = forecastColItemBinding;
            }
        }
    }


}