package com.tiantian.springintejms.service.impl;

import com.tiantian.springintejms.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jms.core.SessionCallback;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.Serializable;

@Component
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    private JmsTemplate jmsTemplate;
//    @Autowired
//    @Qualifier("responseQueue")
//    private Destination responseDestination;

    @Override
    public void sendMessage(Destination destination, final String message) {
//        System.out.println("---------------生产者发送消息-----------------");
//        System.out.println("---------------生产者发了一个消息：" + message);

//        jmsTemplate.setDeliveryMode(DeliveryMode.NON_PERSISTENT); //ztp 设置

        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
				/*TextMessage textMessage = session.createTextMessage(message);
				textMessage.setJMSReplyTo(responseDestination);
				return textMessage;*/
                return session.createTextMessage(message);
            }
        });
    }

    @Override
    public void sendMessage(final Destination destination, final Serializable obj) {
        //未使用MessageConverter的情况
		/*jmsTemplate.send(destination, new MessageCreator() {

			public Message createMessage(Session session) throws JMSException {
				ObjectMessage objMessage = session.createObjectMessage(obj);
				return objMessage;
			}
			
		});*/
        //使用MessageConverter的情况
        jmsTemplate.convertAndSend(destination, obj);
        jmsTemplate.execute(new SessionCallback<Object>() {

            @Override
            public Object doInJms(Session session) throws JMSException {
                MessageProducer producer = session.createProducer(destination);
                Message message = session.createObjectMessage(obj);
                producer.send(message);
                return null;
            }

        });
        jmsTemplate.execute(new ProducerCallback<Object>() {

            @Override
            public Object doInJms(Session session, MessageProducer producer)
                    throws JMSException {
                Message message = session.createObjectMessage(obj);
                producer.send(destination, message);
                return null;
            }

        });
    }

}
