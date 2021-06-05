package com.weather.forecast.service.api;

import java.util.List;

import com.weather.forecast.dto.WeatherForecastResponse;

public interface WeatherForecastService
{
    /**
    * Get weather data for a particular city
    * @param city {@link String} for which weather data is required
    * @return {@link WeatherForecastResponse}
    */
    public List<WeatherForecastResponse> getCurrentWeatherData(String city);
}
