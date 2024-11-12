# 确认机制

## 发布确认机制

**问题：**如果生产者 P 投递消息到交换机 X 的过程中，出现了网络延迟，导致消息丢失，怎么保证消息安全?

<img src="./assets/image-20241112202018694.png" alt="image-20241112202018694" style="zoom:50%;" />

**解决办法：**生产者 P 投递消息到交换机 X 的过程中，交换机 X 会给生产者 P一个 ACK 确认回调，生产者可以根据收到 ACK 值知道是否投递成功。生产者可以根据这些通知采取相应的措施，如重试发送。

<img src="./assets/image-20241112202141953.png" alt="image-20241112202141953" style="zoom:50%;" />

**具体实现办法：**通过 `RabbitTemplate` 的 `setConfirmCallback`

```java
rabbitTemplate.setConfirmCallback( ((correlationData, ack, cause) -> {
            String id = correlationData != null ?correlationData.getId():"";
            if(ack){
                log.info("交换机已经收到id:{}消息了",id);
            }else{
                log.info("交换机没有收到id:{}消息,原因是：{}",id,cause);
            }
        }));
```



## 回退机制

**问题：**当生产者 P 投递消息到交换机  的过程中，消息确定收到了，但是路由配置错误，或者没有绑定队列，此时又如何保证消息安全性?

<img src="./assets/image-20241112202939645.png" alt="image-20241112202939645" style="zoom:50%;" />

**解决办法：**仅仅开启确认机制无法保证消息安全性，可以通过回退机制，使得生产者能够收到通知并采取相应的措施。

<img src="./assets/image-20241112203538655.png" alt="image-20241112203538655" style="zoom:50%;" />

**具体实现办法：**通过 `RabbitTemplate` 的 `setReturnCallback` 方法来实现。当消息无法路由到任何队列时，RabbitMQ 会将消息返回给生产者。**生产者**可以通过设置回调函数来处理这些**未路由**的消息。

```java
rabbitTemplate.setReturnsCallback((returned -> {
            log.info("消息：{}被Return了, 原因：{}， 交换机:{}，路由键:{}",
                    new String(returned.getMessage().getBody()),
                    returned.getReplyText(),
                    returned.getExchange(),
                    returned.getRoutingKey());
        }));
```



## 备份交换机

**备份交换机概述**

备份交换机是一种特殊类型的交换机，它用于接收那些无法路由到任何队列的消息。当消息被发送到主交换机时，<u>如果没有任何队列匹配路由键</u>，消息将被转发到备份交换机。备份交换机可以是任何类型的交换机，例如直接交换机、主题交换机等。

<img src="./assets/image-20241112205502484.png" alt="image-20241112205502484" style="zoom: 50%;" />

**具体实现办法：**配置一个带有备份交换机（Alternate Exchange）的确认交换机（Confirm Exchange）。这个配置确保当消息无法路由到任何队列时，消息会被发送到备份交换机而不是丢失。

```java
@Bean("confirmExchange")
public DirectExchange confirmExchange() {
    return ExchangeBuilder.
            directExchange(CONFIRM_EXCHANGE_NAME).
            durable(true).
            withArgument("alternate-exchange", BACK_EXCHANGE_NAME).
            build();
}
```

> `withArgument` 方法用于向交换机、队列或绑定添加额外的参数。这些参数可以用来配置各种高级功能，例如备份交换机、死信队列、TTL（Time-To-Live）等。
>
> 在这个例子中，`alternate-exchange` 参数的值被设置为 `BACK_EXCHANGE_NAME`，表示当消息无法路由到任何队列时，消息会被发送到名为 `BACK_EXCHANGE_NAME` 的备份交换机。

## 消费者确认机制

## `具体实现代码`

**1.配置application.yml**

```yml
spring:
  rabbitmq:
    host: 118.31.104.65
    port: 5672
    username: bwh
    password: 123456
```

**2.配置 RabbitMQ**

```java
@Configuration
@EnableRabbit
@Slf4j
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
//        template.setMandatory(true);// 启用返回未路由消息的功能
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
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());

        rabbitTemplate.setConfirmCallback( ((correlationData, ack, cause) -> {
            String id = correlationData != null ?correlationData.getId():"";
            if(ack){
                log.info("交换机已经收到id:{}消息了",id);
            }else{
                log.info("交换机没有收到id:{}消息,原因是：{}",id,cause);
            }
        }));

        rabbitTemplate.setMandatory(true);//启用返回未路由消息的功能

        rabbitTemplate.setReturnsCallback((returned -> {
            log.info("消息：{}被Return了, 原因：{}， 交换机:{}，路由键:{}",
                    new String(returned.getMessage().getBody()),
                    returned.getReplyText(),
                    returned.getExchange(),
                    returned.getRoutingKey());
        }));
        return rabbitTemplate;
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

```



# 死信队列



# 延迟队列

