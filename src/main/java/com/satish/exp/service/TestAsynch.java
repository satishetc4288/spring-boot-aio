package com.satish.exp.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class TestAsynch {

    @Autowired
    @Qualifier("asyncExecutor")
    private Executor asyncExecutor;

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
                                     log.info("{}, this is thread: {}", elem, Thread.currentThread().getName()), asyncExecutor))
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
                        () -> number > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(number)).noneMatch(elem -> number % elem == 0),
                        asyncExecutor
                );
    }

}
