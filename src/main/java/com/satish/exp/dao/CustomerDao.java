package com.satish.exp.dao;

import com.satish.exp.model.Customer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CustomerDao {

    private static void sleepThread(int i){
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Customer> getCustomers() {
        return IntStream.range(0,50)
                .peek(CustomerDao::sleepThread)
                .peek(System.out::println)
                .mapToObj( i -> new Customer(i, "customer: " + i))
                .collect(Collectors.toList());
    }

    public Flux<Customer> getCustomersStream() {
        return Flux.range(0,50)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .map( i -> new Customer(i, "customer: " + i));
    }
}
