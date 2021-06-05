package com.weather.forecast.presentday.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.forecast.constants.WeatherStackConstants;
import com.weather.forecast.elastic.service.ElasticSearchService;
import com.weather.forecast.weatherstack.service.WeatherStackService;

@Component
public class PresentDayDataInitializer implements ApplicationRunner
{
    final static Logger logger = LoggerFactory.getLogger(PresentDayDataInitializer.class);

    private WeatherStackService weatherStackService;

    private ElasticSearchService elasticSearchService;

    private ObjectMapper mapper;

    @Autowired
    public PresentDayDataInitializer(WeatherStackService weatherStackService, ElasticSearchService elasticSearchService, ObjectMapper mapper)
    {
        this.weatherStackService = weatherStackService;
        this.elasticSearchService = elasticSearchService;
        this.mapper = mapper;
    }

    @Override
    public void run(ApplicationArguments args)
    {
        try
        {
            List<String> cities = weatherStackService.getListOfCitiesTobeLoadedOnStartup();
            logger.info("Initializing present day weather data for pre configured cities {} ", cities);

            List<String> weatherDataResponse = new ArrayList<>();
            for(String city : cities)
            {
                ResponseEntity<String> response = weatherStackService.getWeatherForecastData(WeatherStackConstants.CURRENT, city);
                logger.info("WeatherStack response for the city {}. Response: {} ", city, response);
                Map<String, Object> responseMap = mapper.readValue(response.getBody(), Map.class);
                if(response.getStatusCode() == HttpStatus.OK && (Boolean) responseMap.getOrDefault("success", true))
                {
                    weatherDataResponse.add(response.getBody());
                }
            }

            if(CollectionUtils.isNotEmpty(weatherDataResponse))
            {
                BulkResponse bulkResponse = elasticSearchService.executeBulk(weatherDataResponse);
            }
            else
            {
                logger.error("Failed to initialize data during startup, WeatherStack is unavailable. Cities: {}", cities);
            }
        }
        catch(Exception ex)
        {
            logger.error("run() Exception while initializing present day data. ExceptionMessage: {} ", ex);
        }
    }
}
