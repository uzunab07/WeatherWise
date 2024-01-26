package edu.uncc.weather;

public class WeatherDescription {
    String id,
            main,
            description,
            icon;

    public WeatherDescription(String id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "WeatherDescription{" +
                "id='" + id + '\'' +
                ", main='" + main + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
