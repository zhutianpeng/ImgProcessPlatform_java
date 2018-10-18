package com.tiantian.springintejms.controller;

import com.tiantian.springintejms.service.ProducerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by xin on 15-2-8 下午8:46
 *
 * @project activeMQ
 * @package com.tiantian.springintejms.controller
 * @Description
 * @blog http://blog.csdn.net/u011439289
 * @email 888xin@sina.com
 * @github https://github.com/888xin
 */

@Controller
@RequestMapping("mq")
public class SenderController {

    @Autowired
    private ProducerService producerService;
    @Autowired
    private JedisPool jedisPool;

//    @Autowired
//    @Qualifier("queueDestination")
//    private Destination destination;

//    @Autowired
//    @Qualifier("topicDestination")
//    private Destination topicDestination ;

//    这里的方法通过接口调用到，测试使用
    @RequestMapping("/send")
    public void send(){
        ActiveMQQueue destination = new ActiveMQQueue("video");
        for (int i=0; i<2; i++) {
            producerService.sendMessage(destination, "你好，生产者！这是消息：" + (i+1));
        }
    }

    @RequestMapping("/saveToRedis")
    public void saveToRedis(){
        String userDestination = "/user/123/video";
        ActiveMQQueue destination = new ActiveMQQueue(userDestination);
        Jedis jedis = jedisPool.getResource();
        while(true){
            List<String> list = jedis.lrange("image_queue_tasklist_done",0,100);
            for(String q:list){
                System.out.println(q);
            }
            producerService.sendMessage(destination, "你好，生产者！这是消息：" );
        }
//        jedis.close();

    }
}
