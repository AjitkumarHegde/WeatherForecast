package com.weather.forecast.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastResponse implements Serializable
{
    @JsonProperty("City")
    private String city;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Latitude")
    private String latitude;

    @JsonProperty("Longitude")
    private String longitude;

    @JsonProperty("Timezone")
    private String timezone;

    @JsonProperty("LocalTime")
    private String localTime;

    @JsonProperty("Temperature")
    private String temperature;

    @JsonProperty("WeatherDescription")
    private String weatherDescription;

    @JsonProperty("WindSpeed")
    private String windSpeed;

    @JsonProperty("WindDegree")
    private String windDegree;

    @JsonProperty("WindDirection")
    private String windDirection;

    @JsonProperty("Pressure")
    private String pressure;

    @JsonProperty("Humidity")
    private String humidity;

    @JsonProperty("FeelsLike")
    private String feelsLike;

    @JsonProperty("Visibility")
    private String visibility;

    @JsonProperty("CloudCover")
    private String cloudCover;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getLocalTime() {
        return localTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDegree() {
        return windDegree;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getPressure() {
        return pressure;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getCloudCover() {
        return cloudCover;
    }

    private WeatherForecastResponse(Builder builder)
    {
        this.city = builder.city;
        this.country = builder.country;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.timezone = builder.timezone;
        this.localTime = builder.localTime;
        this.temperature = builder.temperature;
        this.weatherDescription = builder.weatherDescription;
        this.windSpeed = builder.windSpeed;
        this.windDegree = builder.windDegree;
        this.windDirection = builder.windDirection;
        this.pressure = builder.pressure;
        this.humidity = builder.humidity;
        this.feelsLike = builder.feelsLike;
        this.visibility = builder.visibility;
        this.cloudCover = builder.cloudCover;
    }

    public static class Builder
    {
        private String city;

        private String country;

        private String latitude;

        private String longitude;

        private String timezone;

        private String localTime;

        private String temperature;

        private String weatherDescription;

        private String windSpeed;

        private String windDegree;

        private String windDirection;

        private String pressure;

        private String humidity;

        private String feelsLike;

        private String visibility;

        private String cloudCover;

        public Builder city(String city)
        {
            this.city = city;
            return this;
        }

        public Builder country(String country)
        {
            this.country = country;
            return this;
        }

        public Builder latitude(String latitude)
        {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(String longitude)
        {
            this.longitude = longitude;
            return this;
        }

        public Builder timezone(String timezone)
        {
            this.timezone = timezone;
            return this;
        }

        public Builder localTime(String localTime)
        {
            this.localTime = localTime;
            return this;
        }

        public Builder temperature(String temperature)
        {
            this.temperature = temperature;
            return this;
        }

        public Builder weatherDescription(String weatherDescription)
        {
            this.weatherDescription = weatherDescription;
            return this;
        }

        public Builder windSpeed(String windSpeed)
        {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder windDegree(String windDegree)
        {
            this.windDegree = windDegree;
            return this;
        }

        public Builder windDirection(String windDirection)
        {
            this.windDirection = windDirection;
            return this;
        }

        public Builder pressure(String pressure)
        {
            this.pressure = pressure;
            return this;
        }

        public Builder humidity(String humidity)
        {
            this.humidity = humidity;
            return this;
        }

        public Builder feelsLike(String feelsLike)
        {
            this.feelsLike = feelsLike;
            return this;
        }

        public Builder visibility(String visibility)
        {
            this.visibility = visibility;
            return this;
        }

        public Builder cloudCover(String cloudCover)
        {
            this.cloudCover = cloudCover;
            return this;
        }

        /**
        * Creates weather response object
        * @return {@link WeatherForecastResponse}
        */
        public WeatherForecastResponse build()
        {
            WeatherForecastResponse response = new WeatherForecastResponse(this);
            return response;
        }
    }
}
