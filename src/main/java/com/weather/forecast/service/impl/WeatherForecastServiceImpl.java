package com.weather.forecast.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.forecast.constants.WeatherStackConstants;
import com.weather.forecast.dto.WeatherForecastResponse;
import com.weather.forecast.elastic.service.ElasticSearchService;
import com.weather.forecast.service.api.WeatherForecastService;
import com.weather.forecast.weatherstack.service.WeatherStackService;

@Service
public class WeatherForecastServiceImpl implements WeatherForecastService
{
    final static Logger logger = LoggerFactory.getLogger(WeatherForecastServiceImpl.class);

    private WeatherStackService weatherStackService;

    private ElasticSearchService elasticSearchService;

    private ObjectMapper mapper;

    private static final String SEARCH_KEY = "location.name";

    @Autowired
    public WeatherForecastServiceImpl(WeatherStackService weatherStackService, ElasticSearchService elasticSearchService
            , ObjectMapper mapper)
    {
        this.weatherStackService = weatherStackService;
        this.elasticSearchService = elasticSearchService;
        this.mapper = mapper;
    }

    @Override
    public List<WeatherForecastResponse> getCurrentWeatherData(String city)
    {
        List<WeatherForecastResponse> weatherForecastResponses = new ArrayList<>();
        try
        {
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put(SEARCH_KEY, city);
            SearchRequest searchRequest = elasticSearchService.buildSearchRequest(queryMap);
            SearchResponse searchResponse = elasticSearchService.executeSearch(searchRequest);

            if(searchResponse == null || searchResponse.getHits().getHits().length < 1)
            {
                logger.info("Weather data is unavailable for the city {} on ElasticServer. Fetching from WeatherStack", city);
                ResponseEntity<String> response = weatherStackService.getWeatherForecastData(WeatherStackConstants.CURRENT, city);
                Map<String, Object> responseMap = mapper.readValue(response.getBody(), Map.class);

                //If API returns 200 and the response contains success
                if(response.getStatusCode() == HttpStatus.OK && (Boolean) responseMap.getOrDefault("success", true))
                {
                    logger.info("Weather data fetched successfully for the city {} from WeatherStack", city);
                    WeatherForecastResponse weatherForecastResponse = buildResponse(responseMap);
                    weatherForecastResponses.add(weatherForecastResponse);
                }
                else
                {
                    throw new RuntimeException("Exception while fetching data from WeatherStack, Response received - " + response);
                }
            }
            else
            {
                logger.info("Weather data fetched successfully for the city {} from ElasticServer", city);
                SearchHit[] searchHits = searchResponse.getHits().getHits();
                for (SearchHit hit : searchHits)
                {
                    Map<String, Object> responseMap = mapper.readValue(hit.getSourceAsString(), Map.class);
                    WeatherForecastResponse weatherForecastResponse = buildResponse(responseMap);
                    weatherForecastResponses.add(weatherForecastResponse);
                }
            }
        }
        catch(Exception ex)
        {
            throw new RuntimeException("Exception while fetching weather data for the city " + city, ex);
        }
        return weatherForecastResponses;
    }

    private WeatherForecastResponse buildResponse(Map<String, Object> response)
    {
        Map<String, Object> location = (Map) response.get("location");
        Map<String, Object> current = (Map) response.get("current");
        WeatherForecastResponse weatherForecastResponse = new WeatherForecastResponse.Builder()
                .city(String.valueOf(location.get("name"))).country(String.valueOf(location.get("country")))
                .latitude(String.valueOf(location.get("lat"))).longitude(String.valueOf(location.get("lon")))
                .timezone(String.valueOf(location.get("timezone_id"))).localTime(String.valueOf(location.get("localtime")))
                .temperature(String.valueOf(current.get("temperature"))).windSpeed(String.valueOf(current.get("wind_speed")))
                .windDegree(String.valueOf(current.get("wind_degree"))).windDirection(String.valueOf(current.get("wind_dir")))
                .pressure(String.valueOf(current.get("pressure"))).humidity(String.valueOf(current.get("humidity")))
                .feelsLike(String.valueOf(current.get("feelslike"))).visibility(String.valueOf(current.get("visibility")))
                .cloudCover(String.valueOf(current.get("cloudcover"))).build();
        return weatherForecastResponse;
    }
}
