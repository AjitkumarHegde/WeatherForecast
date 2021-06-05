package com.weather.forecast.weatherstack.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.weather.forecast.configuration.WeatherStackConfiguration;
import com.weather.forecast.constants.WeatherStackConstants;

@Component
public class WeatherStackService
{
    final static Logger logger = LoggerFactory.getLogger(WeatherStackService.class);

    protected final ParameterizedTypeReference<String> restApiEdgeResponseParameterizedTypeReference = new ParameterizedTypeReference<String>()
    {
    };

    private WeatherStackConfiguration weatherStackConfiguration;

    @Autowired
    public WeatherStackService(WeatherStackConfiguration weatherStackConfiguration)
    {
        this.weatherStackConfiguration = weatherStackConfiguration;
    }

    /**
    * Invoke WeatherStack server to fetch weather data for a city.
    * @param typeOfData {@link String} current/historical
    * @param city {@link String} for which weather data is required
    * @return {@link ResponseEntity}
    */
    public ResponseEntity<String> getWeatherForecastData(String typeOfData, String city)
    {
        String url = weatherStackConfiguration.getWeatherStackBaseUrl().concat(typeOfData);
        logger.info("getWeatherForecastData() WeatherStack API Url: {} ", url);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        builder.queryParam(WeatherStackConstants.ACCESS_KEY, new String(Base64.getDecoder().decode(WeatherStackConstants.API_KEY), StandardCharsets.UTF_8))
            .queryParam(WeatherStackConstants.QUERY, city);
        ResponseEntity<String> weatherDataForecastResponse = weatherStackConfiguration.getRestTemplate().exchange(
                builder.build(false).toUriString(), HttpMethod.GET, entity,
                restApiEdgeResponseParameterizedTypeReference);
        logger.debug("getWeatherForecastData() Received response from WeatherStack: {} ", weatherDataForecastResponse.getBody());
        return weatherDataForecastResponse;
    }

    public List<String> getListOfCitiesTobeLoadedOnStartup()
    {
        return Arrays.asList(weatherStackConfiguration.getLoadWeatherDataOnStartupCitiesList().split(","));
    }
}
