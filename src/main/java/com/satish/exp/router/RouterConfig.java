package com.satish.exp.router;

import com.satish.exp.handler.CustomerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
    @Autowired
    private CustomerHandler customerHandler;
    @Bean
    public RouterFunction<ServerResponse> getRouting(){
        return RouterFunctions
                .route()
                .GET("/router/customers", customerHandler::loadCustomers)
                .GET("/router/customer/stream", customerHandler::getCustomersStream)
                .GET("/router/customer/{input}", customerHandler::findCustomers)
                .POST("/create/customer", customerHandler::saveCustomer)
                .build();
    }
}
