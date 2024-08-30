package com.satish.exp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@AllArgsConstructor
public class HttpWebService {
    @Autowired
    private final RestTemplate restTemplate;

    public ResponseEntity<String> getWeatherData(Float latitude, Float longitude){
        HttpEntity<String> re = new HttpEntity<>("satish");
        return restTemplate.exchange(
"https://api.open-meteo.com/v1/forecast?" +
        "latitude={latitude}&" +
        "longitude={longitude}&" +
        "current=temperature_2m,wind_speed_10m&" +
        "hourly=temperature_2m,relative_humidity_2m,wind_speed_10m",
                HttpMethod.GET, re , String.class, latitude, longitude);
    }

}
