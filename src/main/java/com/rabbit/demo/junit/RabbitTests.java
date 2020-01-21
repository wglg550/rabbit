package com.rabbit.demo.junit;

import com.rabbit.demo.DemoApplication;
import com.rabbit.demo.util.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = DemoApplication.class)
public class RabbitTests {

    @Autowired
    private Sender sender;

    @Test
    public void sendTest() throws Exception {
//        while (true) {
        String msg = new Date().toString();
        sender.convertSendAndReceive1(msg);
        Thread.sleep(1000);
//        }
    }
}
