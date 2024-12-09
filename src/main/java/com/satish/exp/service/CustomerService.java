package com.satish.exp.service;

import com.google.common.collect.Lists;
import com.satish.exp.dao.CustomerDao;
import com.satish.exp.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    public List<Customer> loadCustomers(){
        return customerDao.getCustomers();
    }

    public Flux<Customer> loadCustomersStream(){
        return customerDao.getCustomersStream();
    }

    public static void main(String[] args) {
        List<Integer> list = IntStream.range(1 , 100).mapToObj(str -> new Integer(str)).collect(Collectors.toList());
        for(List<Integer> smallList: Lists.partition(list,10)){
            System.out.println("batch 1 size: " + smallList.size());
            smallList.forEach(System.out::println);
        }
    }
}
