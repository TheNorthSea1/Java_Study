package cn.sycoder.springbootrabbitmqsend02.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 作者：上云
 * 网址：http://www.sycoder.cn
 * 日期：2023/1/9
 */
@Component
@Slf4j
public class ReturnCallBack implements RabbitTemplate.ReturnsCallback {
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        Message message = returned.getMessage();
        log.info("消息：{}被服务器退回，退回原因：{}，退回码：{}",
               message.getBody(),
                returned.getReplyText(),
                returned.getReplyCode());
        /**
         * 处理方式
         *  1：尝试重新调用
         *  2：落库处理，存入 mysql 数据库中
         */
    }
}
