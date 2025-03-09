package com.satish.exp.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class TestAsynch {

    @Async("asyncExecutor")
    public CompletableFuture<String> runAsynch(){
        List<Integer> data = IntStream.range(0,1000).boxed().collect(Collectors.toList());
        List<List<Integer>> list = ListUtils.partition(data, 5);
        list.forEach(list1 -> {
            List<CompletableFuture<Void>> futures =
                    list1
                    .stream()
                    .map(elem ->
                            CompletableFuture.runAsync( () ->
                                    log.info("{}, this is thread: {}", elem, Thread.currentThread().getName())))
                            .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.info("this is thread: {}", Thread.currentThread().getName());
        });
        return CompletableFuture.completedFuture("data");
    }

    @Async("asyncExecutor")
    public CompletableFuture<Boolean> isPrime(Integer number){
        return CompletableFuture
                .supplyAsync(
                        () -> IntStream.range(2, number - 1).boxed().noneMatch(elem -> number % elem == 0)
                );
    }

}
