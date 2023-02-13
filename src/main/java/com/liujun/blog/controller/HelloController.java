package com.liujun.blog.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String Hello () {
        return "hello world";
    }
}
