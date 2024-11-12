package cn.sycoder.springbootrabbitmqsend02.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 作者：上云
 * 网址：http://www.sycoder.cn
 * 日期：2023/1/9
 */
@Component
@Slf4j
public class AckCallBack implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ?correlationData.getId():"";
        if(ack){
            log.info("交换机已经收到id:{}消息了",id);
        }else{
            log.info("交换机没有收到id:{}消息,原因是：{}",id,cause);
        }
    }

}
