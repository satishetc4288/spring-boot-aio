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
        try (var inputStream = Customer.class.getClassLoader().getResourceAsStream("test.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("test.json not found on classpath");
            }
            try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream, java.nio.charset.StandardCharsets.UTF_8))) {
                List<Customers> customers = reader.lines()
                        .filter(str -> str.startsWith("{\"customers\":"))
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
    }
}

