package com.satish.exp.controller;

import com.satish.exp.model.Customer;
import com.satish.exp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class AsynchController {

    @Autowired
    private CustomerService customerService;
    @GetMapping("/sync")
    public List<Customer> getCustomer(){
        return customerService.loadCustomers();
    }

    @GetMapping(value = "/async", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getCustomerAsync(){
        return customerService.loadCustomersStream();
    }
}
