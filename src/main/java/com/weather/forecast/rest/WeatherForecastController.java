package com.weather.forecast.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.forecast.dto.WeatherForecastResponse;
import com.weather.forecast.service.api.WeatherForecastService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/weather")
@RestController
@Api(tags = "WeatherForecastController", produces = "application/json")
public class WeatherForecastController
{
    final static Logger logger = LoggerFactory.getLogger(WeatherForecastController.class);

    @Autowired
    private WeatherForecastService weatherForecastService;


    @RequestMapping(value = "/current", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Get weather data for a particular city", notes = "Get weather data for a particular city", response = WeatherForecastResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Request contained data that was invalid")})
    public ResponseEntity<WeatherForecastResponse> getCurrentWeatherData(@RequestParam(required = true) String city)
    {
        logger.debug("getCurrentWeatherData() Fetching weather data for the city: {} ", city);
        List<WeatherForecastResponse> weatherForecastResponses = weatherForecastService.getCurrentWeatherData(city);
        ResponseEntity<WeatherForecastResponse> responseEntity = new ResponseEntity(weatherForecastResponses, HttpStatus.OK);
        return responseEntity;
    }
}
