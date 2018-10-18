package com.tiantian.springintejms.controller;

import com.tiantian.springintejms.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by AndrewKing on 8/8/2018.
 */

@Controller
@RequestMapping("mq")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;
}
