package cn.sycoder.springbootrabbitmqsend02;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRabbitmqSend02ApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    void contextLoads() {
        CorrelationData data = new CorrelationData("1");//设置当前的数据id为1
        rabbitTemplate.convertAndSend("confirm-exchange","ack111","你好",data);
    }

}
