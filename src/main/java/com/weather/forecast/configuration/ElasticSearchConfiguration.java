package com.weather.forecast.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ElasticSearchConfiguration
{
    @Value("${elastic.server.host:localhost}")
    private String elasticServerHost;

    @Value("${elastic.server.port:19200}")
    private int elasticServerPort;

    @Value("${current.weather.data.index.name:current_weather_data}")
    private String currentWeatherDataIndexName;

    @Autowired
    private RestTemplate restTemplate;

    public String getElasticServerHost()
    {
        return elasticServerHost;
    }

    public int getElasticServerPort()
    {
        return elasticServerPort;
    }

    public String getCurrentWeatherDataIndexName()
    {
        return currentWeatherDataIndexName;
    }

    public RestTemplate getRestTemplate()
    {
        return restTemplate;
    }
}
