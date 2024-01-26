package edu.uncc.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//Khaled Mohamed Ali and Joseph Mauney

public class MainActivity extends AppCompatActivity implements CitiesFragment.CitiesFragmentListener, CurrentWeatherFragment.cwListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new CitiesFragment())
                .commit();
    }

    @Override
    public void gotoCurrentWeather(DataService.City city) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, CurrentWeatherFragment.newInstance(city))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToForecast(DataService.City mCity) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, WeatherForecastFragment.newInstance(mCity))
                .addToBackStack(null)
                .commit();
    }
}