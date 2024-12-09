package com.satish.exp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Getter
@Setter
public class Greeting {

    private  long id;
    private  String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }
}