package com.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.junit.jupiter.api.Test;

public class TestMe {

    @Test
    public void testMono(){
        Mono<?> monoString = Mono.just("justString").then(Mono.just("justString 2")).
                then(Mono.error(new RuntimeException("Exception Occurred"))).log();
        monoString.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }

    @Test
    public void testFlux(){
        Flux<String> stringFlux = Flux.just("satish", "singh", "rajput", "cdk global")
                .concatWithValues("AWS")
                .concatWith(Flux.error(new RuntimeException("This is error")))
                .concatWithValues("after error")
                .log();
        stringFlux.subscribe(System.out::println, (e) -> System.out.println(e.getMessage()));
    }
}
