package com.satish.exp.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AmqpSubscriber {

    //@RabbitListener(queues = "test-java-queue")
    //public void processMessage(String content) {System.out.println("########### message received " + content);}

}
