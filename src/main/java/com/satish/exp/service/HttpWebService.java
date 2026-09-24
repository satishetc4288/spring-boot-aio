package com.satish.exp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class HttpWebService {
    
    private final WebClient webClient = WebClient.create();

    public Mono<String> getWeatherData(Float latitude, Float longitude){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.open-meteo.com")
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current", "temperature_2m,wind_speed_10m")
                        .queryParam("hourly", "temperature_2m,relative_humidity_2m,wind_speed_10m")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getWeatherData(){
        return webClient.get()
                .uri("https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&past_days=10&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(res -> log.info("Successfully fetched weather data"));
    }

}
