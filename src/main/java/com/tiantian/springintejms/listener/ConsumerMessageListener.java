package com.tiantian.springintejms.listener;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author AndrewKing
 */
public class ConsumerMessageListener implements MessageListener {
    @Autowired
    private JedisPool jedisPool;
//    @Autowired
//    private RedisTemplate jedisTemplate;

    @Override
    public void onMessage(Message message) {
//      此处用于测试
        if(message instanceof TextMessage){
            TextMessage textMsg = (TextMessage) message;
            System.out.println("接收到一个纯文本消息。");
            try {
                System.out.println("ztp消息内容是：" + textMsg.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
//      此处才是正常接受的数据类型
        }else if (message instanceof BytesMessage) {
            BytesMessage bytesMessage = (BytesMessage)message;
            try {
                // 得到一些参数：

                String user_token = String.valueOf(bytesMessage.getByteProperty("user-token"));  // user-token
                String task = String.valueOf(bytesMessage.getByteProperty("task"));               // task
                String image_base64 ="";                                            // origin image
                UUID uuid= UUID.randomUUID();
                String imageID = uuid.toString();                                   // imageID

                byte[] buffer = new byte[1024*1024];
                int len = 0;
                while((len=bytesMessage.readBytes(buffer))!=-1){
                    image_base64 = new String(buffer,0,len);
                }

//                System.out.println("imageID: "+imageID +",user-token: "+user_token+" ,task: "+task+", image_base64: "+ image_base64);
                Jedis jedis = jedisPool.getResource();


                Map<String,String> imageData = new HashMap<String, String>();
                imageData.put("imageID",imageID);
                imageData.put("userToken",user_token);
                imageData.put("taskList",task);
                imageData.put("image",image_base64);

                JSONObject jsonObject = JSONObject.fromObject(imageData);

//              比较task列表并分发存入对应的redis的list

                int taskToDo = Integer.parseInt(task);
                if((taskToDo&0x01)>0) { //pose
                    jedis.rpush("image_queue_to_pose_estimation", jsonObject.toString());
//                    jedisTemplate.convertAndSend("test_channel",jsonObject);
                }
                if((taskToDo&0x02)>0){ //face
                    jedis.rpush("image_queue_to_face_recognition", jsonObject.toString());
                }
                if((taskToDo&0x04)>0){ //object
                    jedis.rpush("image_queue_to_object_recognition", jsonObject.toString());
                }

                //手动释放资源，不然会因为jedisPool里面的maxActive=200的限制，只能创建200个jedis资源。
                jedis.close();
//                  jedisPool.returnResource(jedis); //此方法弃用

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
