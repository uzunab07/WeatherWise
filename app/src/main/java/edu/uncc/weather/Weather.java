package edu.uncc.weather;

import java.util.ArrayList;

public class Weather {
    Main main;
    Wind wind;
    Clouds clouds;
    ArrayList<WeatherDescription> weather;

    @Override
    public String toString() {
        return "Weather{" +
                "main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", weather=" + weather +
                '}';
    }
}
