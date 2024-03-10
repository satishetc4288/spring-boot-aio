package com.satish.exp.service;

import com.satish.exp.dao.CustomerDao;
import com.satish.exp.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

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
}
