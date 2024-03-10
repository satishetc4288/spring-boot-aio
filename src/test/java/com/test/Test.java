package com.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Test {

    @org.junit.jupiter.api.Test
    public void testMono(){
        Mono<?> monoString = Mono.just("justString").then(Mono.just("justString 2")).
                then(Mono.error(new RuntimeException("Exception Occurred"))).log();
        monoString.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }

    @org.junit.jupiter.api.Test
    public void testFlux(){
        Flux<String> stringFlux = Flux.just("satish", "singh", "rajput", "cdk global")
                .concatWithValues("AWS")
                .concatWith(Flux.error(new RuntimeException("This is error")))
                .concatWithValues("after error")
                .log();
        stringFlux.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }
}
