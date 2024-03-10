package com.satish.exp.handler;

import com.satish.exp.dao.CustomerDao;
import com.satish.exp.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandler {
    @Autowired
    private CustomerDao customerDao;

    public Mono<ServerResponse> loadCustomers(ServerRequest request){
        Flux<Customer> customer = customerDao.getCustomerList();
        return  ServerResponse.ok().body(customer, Customer.class);
    }

    public Mono<ServerResponse> getCustomersStream(ServerRequest request){
        Flux<Customer> customer = customerDao.getCustomersStream();
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class);
    }

    public Mono<ServerResponse> findCustomers(ServerRequest request){
        int custId = Integer.valueOf(request.pathVariable("input"));
        Flux<Customer> customer = customerDao.getCustomerList().filter( customer1 -> customer1.getId() == custId);
        return  ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(customer, Customer.class);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request){
        Mono<Customer> customerMono = request.bodyToMono(Customer.class);
        Mono<String> monoString = customerMono.map( data -> data.getId() + " : " + data.getName());
        return ServerResponse.ok().body(monoString, String.class);
    }
}
