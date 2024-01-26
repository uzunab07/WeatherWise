package edu.uncc.weather;

public class Main {
    String temp,feels_like,temp_min, temp_max,pressure,humidity;

    public Main(String temp, String feels_like, String temp_min, String temp_max, String pressure, String humidity) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "temp='" + temp + '\'' +
                ", feels_like='" + feels_like + '\'' +
                ", temp_min='" + temp_min + '\'' +
                ", temp_max='" + temp_max + '\'' +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                '}';
    }
}
