package com.tiantian.springintejms.service.impl;

import com.tiantian.springintejms.service.ProducerService;
import com.tiantian.springintejms.service.RedisSubService;
import com.tiantian.springintejms.utils.FaceUtils;
import com.tiantian.springintejms.utils.PoseUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AndrewKing on 10/14/2018.
 * redis 监听
 */
public class RedisSubServiceImpl implements RedisSubService {
    @Autowired
    private ChannelTopic channelTopic;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private ProducerService producerService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        JSONObject json = JSONObject.fromObject(message.toString());
        String imageID = json.getString("imageID");

        Jedis jedis = jedisPool.getResource();
        String imageContent = jedis.hget(imageID,"image"); //  原图
        String userToken = jedis.hget(imageID,"userToken");//  user

//        output
        String imageResult = imageContent;

//        pose
        String poseResultString = jedis.hget(imageID,"poseResult");
        if(StringUtils.isNotBlank(poseResultString)){
            imageResult = PoseUtils.drawHumans(poseResultString,imageContent);
        }

//        face
        String faceResultString = jedis.hget(imageID,"faceResult");
        if(StringUtils.isNotBlank(faceResultString)){
            imageResult = FaceUtils.drawFaces(faceResultString,imageResult);
        }

        JSONArray poseResultParsed=null;
//        get pose result ArrayList
        if(StringUtils.isNotBlank(poseResultString) && !poseResultString.equals("[]")){
            poseResultParsed = PoseUtils.getPoseData(poseResultString,imageContent);
        }

        Map<String,String> result = new HashMap<String, String>();
        result.put("image",imageResult);

        if(poseResultParsed.isEmpty()){
            result.put("poseResultParsed",poseResultParsed.toString());
        }

        JSONObject output = JSONObject.fromObject(result);


//       redis 释放资源
        jedis.del(imageID);
        jedis.close();

//       activeMQ send for video and poseResultParsed
        ActiveMQQueue destination = new ActiveMQQueue("/user/"+ userToken +"/video");

        producerService.sendMessage(destination,output.toString());

    }
}
