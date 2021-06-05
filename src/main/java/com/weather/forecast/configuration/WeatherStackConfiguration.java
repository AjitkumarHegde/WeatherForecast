package com.weather.forecast.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherStackConfiguration
{
    @Value("${weatherstack.base.url:http://api.weatherstack.com/}")
    private String weatherStackBaseUrl;

    @Value("${weather.data.cities.startup.list}")
    private String loadWeatherDataOnStartupCitiesList;

    @Autowired
    private RestTemplate restTemplate;

    public String getLoadWeatherDataOnStartupCitiesList()
    {
        return loadWeatherDataOnStartupCitiesList;
    }

    public String getWeatherStackBaseUrl()
    {
        return weatherStackBaseUrl;
    }

    public RestTemplate getRestTemplate()
    {
        return restTemplate;
    }
}
