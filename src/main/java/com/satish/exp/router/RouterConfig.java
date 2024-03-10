package com.satish.exp.router;

import com.satish.exp.handler.CustomerHandler;
import com.satish.exp.handler.CustomerStreamHandler;
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

    @Autowired
    private CustomerStreamHandler streamHandler;
    @Bean
    public RouterFunction<ServerResponse> getRouting(){
        return RouterFunctions
                .route()
                .GET("/router/customers", customerHandler::loadCustomers)
                .GET("/router/customer/stream", streamHandler::getCustomers)
                .build();
    }
}
