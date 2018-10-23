package com.tiantian.springintejms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by AndrewKing on 8/8/2018.
 */

@Controller
@RequestMapping("page")
public class PageController {

    @RequestMapping(value="/")
    public String index(){
        return "index";
    }

}
