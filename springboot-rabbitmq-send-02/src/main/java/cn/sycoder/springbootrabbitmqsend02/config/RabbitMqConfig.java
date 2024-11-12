package cn.sycoder.springbootrabbitmqsend02.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 作者：上云
 * 网址：http://www.sycoder.cn
 * 日期：2023/1/9
 */
@Configuration
@EnableRabbit
public class RabbitMqConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm-exchange";
    public static final String BACK_EXCHANGE_NAME = "back-confirm-exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm-queue";
    public static final String BACK_QUEUE_NAME = "back-queue";

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

//    @Bean(name = "myRabbitTemplate")
//    public RabbitTemplate rabbitTemplate(AckCallBack ackCallBack, ReturnCallBack returnCallBack) {
//        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory());
//        template.setConfirmCallback(ackCallBack);
//        template.setMandatory(true);//true交换机回退 false 直接丢弃
//        template.setReturnsCallback(returnCallBack);
//        return template;
//    }
//
//
//    @Bean
//    public CachingConnectionFactory cachingConnectionFactory() {
//        //创建缓存连接工厂
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//
//
//        factory.setHost(host);
//        factory.setPassword(password);
//        factory.setUsername(username);
//        factory.setPort(port);
//        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//        return factory;
//    }
//
//    @Bean("confirmExchange")
//    public DirectExchange confirmExchange() {
//        return ExchangeBuilder.
//                directExchange(CONFIRM_EXCHANGE_NAME).
//                durable(true).
//                withArgument("alternate-exchange",BACK_EXCHANGE_NAME).
//                build();
//    }
//
//    @Bean("confirmQueue")
//    public Queue confirmQueue(){
//        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
//    }
//
//    @Bean
//    public Binding confirmBinding(){
//        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with("ack");
//    }
//    //申明备份交换机
//    @Bean("backExchange")
//    public FanoutExchange backExchange() {
//        return new FanoutExchange(BACK_EXCHANGE_NAME);
//    }
//
//    //申明备份队列
//    @Bean("backQueue")
//    public Queue backQueue(){
//        return QueueBuilder.durable(BACK_QUEUE_NAME).build();
//    }
//    //绑定备份交换机和备份队列之间的关系
//    @Bean
//    public Binding backBinding(){
//        return BindingBuilder.bind(backQueue()).to(backExchange());
//    }


    /**
     * 注释掉的内容是返回机制和回退机制的代码，新代码是备份交换机
     */
    @Bean(name="myRabbitTemplate")
//    @Scope()
    public RabbitTemplate rabbitTemplate(AckCallBack ackCallBack, ReturnCallBack returnCallBack){
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory());
        template.setConfirmCallback(ackCallBack);
        template.setMandatory(true);//true交换机回退 false 直接丢弃
        template.setReturnsCallback(returnCallBack);
        return template;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(){
        //创建缓存连接工厂
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setPassword(password);
        factory.setUsername(username);
        factory.setPort(port);
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return factory;
    }

    //确认交换机
    @Bean
    public DirectExchange confirmExchange(){
       return ExchangeBuilder.
               directExchange(CONFIRM_EXCHANGE_NAME).
               durable(true).
               build();
    }
    //确认队列
    @Bean
    public Queue confirmQueue(){

        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    //绑定交换机和队列之间的关系
    @Bean
    public Binding confirmBinding(){
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with("ack");
    }
}
