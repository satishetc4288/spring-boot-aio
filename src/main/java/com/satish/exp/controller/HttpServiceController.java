package com.satish.exp.controller;

import com.satish.exp.model.Greeting;
import com.satish.exp.service.DaoService;
import com.satish.exp.service.HttpWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/rest")
public class HttpServiceController {

    @Autowired
    private HttpWebService httpWebService;

    @GetMapping("/weather")
    public ResponseEntity<String> getWeatherData(@RequestParam Float latitude, @RequestParam Float longitude){
        return httpWebService.getWeatherData(latitude,longitude);
    }
}
