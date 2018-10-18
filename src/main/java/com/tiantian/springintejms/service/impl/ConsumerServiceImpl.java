package com.tiantian.springintejms.service.impl;

import com.tiantian.springintejms.service.ConsumerService;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void receiveMessage(String message) throws JMSException {
        System.out.println("------------------消费者接收消息-----------------");
        System.out.println("-----------------生产者发来的消息是：" + message);
    }

}
