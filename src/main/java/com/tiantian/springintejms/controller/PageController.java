package com.tiantian.springintejms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by AndrewKing on 8/8/2018.
 */

@Controller
public class PageController {

    @RequestMapping("/index")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @RequestMapping("/test")
    public ModelAndView test(){
        return new ModelAndView("test");
    }

}
