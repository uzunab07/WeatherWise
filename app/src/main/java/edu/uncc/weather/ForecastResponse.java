package edu.uncc.weather;

import java.util.ArrayList;

public class ForecastResponse {
    ArrayList<Forecast> list;

    @Override
    public String toString() {
        return "ForecastResponse{" +
                "list=" + list +
                '}';
    }
}
