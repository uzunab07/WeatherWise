package edu.uncc.weather;

import java.util.ArrayList;

public class Forecast {
    String dt_txt;
    Main main;
    ArrayList<WeatherDescription> weather;

    public Forecast(String dt_txt, Main main, ArrayList<WeatherDescription> weatherDescription) {
        this.dt_txt = dt_txt;
        this.main = main;
        this.weather = weatherDescription;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "dateTime='" + dt_txt + '\'' +
                ", main=" + main +
                ", weatherDescription=" + weather +
                '}';
    }
}
