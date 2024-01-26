package edu.uncc.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.weather.databinding.FragmentCurrentWeatherBinding;
import edu.uncc.weather.databinding.FragmentWeatherForecastBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForecastFragment extends Fragment {

    FragmentWeatherForecastBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private DataService.City mCity;
    ForecastAdapter adapter;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Forecast> forecasts = new ArrayList<>();

    public WeatherForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mCity Parameter 1.
     * @return A new instance of fragment WeatherForcastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherForecastFragment newInstance(DataService.City mCity) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, mCity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (DataService.City) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentWeatherForecastBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    TextView cityName;
    ListView listView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showForecast();

        listView = view.findViewById(R.id.listView);

        cityName = view.findViewById(R.id.textViewCityName);
        cityName.setText(mCity.getCity()+", "+mCity.getCountry());

    }

    public void showForecast(){
        HttpUrl url = HttpUrl.parse("https://api.openweathermap.org/data/2.5/forecast").newBuilder()
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
                    ForecastResponse list = gson.fromJson(response.body().charStream(),ForecastResponse.class);

                    forecasts = list.list;
                    Log.d("demo", "onResponse: "+forecasts);

                    for (int i = 0; i < forecasts.size(); i++){
                        Forecast foretest = forecasts.get(i);
                        Log.d("demo", "onResponse: "+foretest);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ForecastAdapter(getActivity(), R.layout.forecast_row_item, forecasts);
                            listView.setAdapter(adapter);
                        }
                    });
                }else{
                    Log.d("demo", "onFailure: "+response.body());
                }
            }
        });

    }


}