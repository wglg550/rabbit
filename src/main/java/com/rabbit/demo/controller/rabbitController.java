package com.rabbit.demo.controller;

import com.rabbit.demo.util.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rabbit")
public class rabbitController {

    @Autowired
    private Sender sender;

    @GetMapping("/send")
    public void rabbitTest(@RequestParam String name) {
        sender.convertSendAndReceive1(name);
        sender.convertSendAndReceive2("234");
        sender.send1();
        sender.send2();
        sender.send3();
//        sender.sendObj();
    }
}
