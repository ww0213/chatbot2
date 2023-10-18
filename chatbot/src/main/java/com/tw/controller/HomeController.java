package com.tw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // 這裡的 "chat" 指的是 chat.jsp，根據您在 application.properties 中的設定
        return "index";
    }
}
