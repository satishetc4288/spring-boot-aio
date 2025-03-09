package com.satish.exp.controller;

import com.satish.exp.model.Customer;
import com.satish.exp.service.CustomerService;
import com.satish.exp.service.TestAsynch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customers")
public class AsynchController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TestAsynch testAsynch;

    @GetMapping("/sync")
    public List<Customer> getCustomer(){
        return customerService.loadCustomers();
    }

    @GetMapping(value = "/async", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getCustomerAsync(){
        return customerService.loadCustomersStream();
    }

    @GetMapping(value = "/test")
    public CompletableFuture<String> getString(){
        return testAsynch.runAsynch();
    }

    @GetMapping(value = "/isprime/{number}")
    public CompletableFuture<Boolean> getString(@PathVariable Integer number){
        return testAsynch.isPrime(number);
    }
}
