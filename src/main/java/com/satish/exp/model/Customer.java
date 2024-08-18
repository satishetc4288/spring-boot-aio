package com.satish.exp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int id;
    private String name;

    public static void main(String[] args) {
        Flux.range(0,15).
                map( elem -> String.valueOf(elem)).
                delayElements(Duration.ofSeconds(1)).
                subscribe(System.out::println);
    }
}
