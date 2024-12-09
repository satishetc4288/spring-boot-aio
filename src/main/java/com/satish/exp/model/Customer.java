package com.satish.exp.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int id;
    private String name;
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Customers> customers =
            Files.readAllLines(Paths.get("/Users/satish/Documents/workspace/spring-boot-aio/src/main/resources/test.json"))
            .stream().filter(str -> str.startsWith("{\"customers\":"))
            .map(str -> {
                try {
                    return mapper.readValue(str, Customers.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        System.out.println(customers.size());
    }
}

