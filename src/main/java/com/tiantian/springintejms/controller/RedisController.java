package com.tiantian.springintejms.controller;

import com.tiantian.springintejms.service.impl.RedisSubServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by AndrewKing on 10/14/2018.
 */

@Controller
@RequestMapping("redis")
public class RedisController {
    @Autowired
    private RedisSubServiceImpl subService;

//    @RequestMapping(value = "/sub")
//    public void Subscriber() {
//        MessageList messageList = subService.getMessageList();
//    }
}
