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

> Note:
>
> 使用备份交换机后，回退机制会失效。

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

**问题**:如果消费者处理的过程中发生异常，导致消息丢失怎么办?

<img src="./assets/image-20241113144007485.png" alt="image-20241113144007485" style="zoom:50%;" />

**解决办法**：

![image-20241113150029263](./assets/image-20241113150029263.png)

### `basicAck`

![image-20241113192449061](./assets/image-20241113192449061.png)

### `basicReject`

> 拒绝一条消息

![image-20241113192527006](./assets/image-20241113192527006.png)

### `basicNack`

> 拒绝一条或多条消息

![image-20241113192556443](./assets/image-20241113192556443.png)

### `deliveryTag` 

1. **唯一标识消息**：每个消息在队列中都有一个唯一的 `deliveryTag`，确保消息可以被精确地识别和处理。
2. **消息确认**：消费者可以通过 `deliveryTag` 来确认消息已经成功处理，告诉 RabbitMQ 可以安全地删除该消息。
3. **消息拒绝**：消费者可以通过 `deliveryTag` 来拒绝消息，并指定是否重新入队。
4. **消息重试**：消费者可以通过 `deliveryTag` 来重新入队消息，以便其他消费者可以再次尝试处理。

**具体实现办法：**

```yml
spring:
  rabbitmq:
    host: 118.31.104.65
    port: 5672
    username: bwh
    password: 123456
    listener:
      simple:
        retry:
          enabled: true
        acknowledge-mode: manual #手动 ack
```

```java
@RabbitListener(queues = {"back-queue"})
public void receive(String body, Channel channel, Message message) throws IOException {
    try {
        log.info("收到队列信息" + body);
        //开始业务处理
        //            int ret = 1 / 0;
        //业务处理结束
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    } catch (Exception e) {
        //表示消息是否重复处理
        if(message.getMessageProperties().getRedelivered()){
            //拒绝签收
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
        }else{
            //返回队列，重新发送
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }
}
```



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

死信队列（Dead Letter Queue, DLQ）是消息队列系统中的一种特殊队列，用于存放那些无法被正常消费的消息。这些消息可能因为各种原因无法被处理，例如消息过期、消费者拒绝消息、达到最大重试次数等。通过配置死信队列，可以捕获这些消息并进行进一步的处理，从而避免消息丢失。

**死信队列**：Dead Letter Queue用来存放死信消息的队列

**死信交换机**：Dead Letter Exchange用来路由死信消息的交换机

## **常见的死信场景**

1. **消息过期**：消息在队列中的生存时间超过了预设的时间限制(TTL)。
2. **消息被拒绝**：消费者调用 `basicNack` 或 `basicReject` 方法拒绝消息，并且设置了 `requeue` 参数为 `false`。
3. **队列达到最大长度**：队列中的消息数量达到了预设的最大值

## **架构图**

![image-20241113153647815](./assets/image-20241113153647815.png)

## TTL（生存时间）(⭐️)

> **TTL**概述：用来控制消息或者是队列的最大存活时间，单位是毫秒。
>
> **设置TTL两种方式：**
>
> 1. 给消息设置TTL：在发送消息时指定该消息的有效时间。
>
> 2. 给队列设置TTL：声明队列时指定该队列中所有消息的有效时间。
>
> **注意：**如果同时配置了队列的 TTL 和消息的 TTL，那么较小的那个值将会被使用。
>
> #### 区别总结
>
> 1. **作用对象不同**：
>    - **消息 TTL**：作用于单个消息，可以为每个消息设置不同的 TTL。
>    - **队列 TTL**：作用于整个队列，队列中的所有消息都遵循相同的 TTL。
> 2. **配置方式不同**：
>    - **消息 TTL**：在发送消息时通过 `MessagePostProcessor` 设置。
>    - **队列 TTL**：在声明队列时通过队列参数设置。
> 3. **灵活性不同**：
>    - **消息 TTL**：提供了更高的灵活性，可以根据具体业务需求为每个消息设置不同的 TTL。
>
> # 主要区别⭐️⭐️
>
> 1、**在普通队列中，`RabbitMQ`是等消息到达队列顶部即将被消费时，才会判断其是否过期并删除或者移至死信队列。**
>
>   假设你有一个队列 `my_queue`，其中包含以下消息：
>
> 1. 消息 A：永久消息（没有 TTL）
>
> 2. 消息 B：TTL 为 10 秒
>
>    消息 A、B  进入队列。
>
> 3. 只要，A没有被消费，B即使已经超过生存时间，也不会消失。
>
> 2、**队列设置过期是直接丢弃或丢到死信队列**

- ### 图形界面方式设置

<img src="./assets/image-20241113155214191.png" alt="image-20241113155214191" style="zoom:67%;" />

<img src="./assets/image-20241113161505743.png" alt="image-20241113161505743" style="zoom:67%;" />

- ### 代码方式

  - **消息 TTL**

    ```java
    @Test
    void contextLoads() {
        CorrelationData data = new CorrelationData("1");//设置当前的数据id为1
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties properties = message.getMessageProperties();
                properties.setExpiration("5000");
                return message;
            }
        };
        //        rabbitTemplate.convertAndSend("test-exchange","ack","你好",processor,data);
        rabbitTemplate.convertAndSend("test-exchange", "ack", "你好", processor,data);
    }
    ```

  - 队列TTL

    ```java
    @Bean("confirmQueue")
        public Queue confirmQueue(){
            Map<String, Object> map = new HashMap<>();
            // 指定死信交换机
    //        map.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME);
            //限制队列最大长度
    //        map.put("x-max-length",10);
            //设置队列的过期时间
            map.put("x-message-ttl",5000);
            return QueueBuilder.durable(TEST_QUEUE_NAME).withArguments(map).build();//设置过期队列
        }
    ```

    > ### Note:
    >
    > map的 key具体写什么，可以通过RabbitMQ图形化界面，增加队列中Argument进行查看。
    >
    > <img src="./assets/image-20241113181824120.png" alt="image-20241113181824120" style="zoom: 67%;" />

## 死信队列实现（⭐️）

1. #### 申明死信交换机

   ```java
   @Bean("deadExchange")
   public DirectExchange deadExchange() {
       return new DirectExchange(DEAD_EXCHANGE_NAME);
   }
   ```

2. #### 申明死信队列

   ```java
   @Bean("deadQueue")
   public Queue deadQueue(){
       return QueueBuilder.durable(DEAD_QUEUE_NAME).build();
   }
   ```

3. #### 绑定死信交换机和死信队列之间的关系

   ```java
   @Bean
   public Binding deadBinding(){
       return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("ack");
   }
   ```

4. #### 将死信交换机绑定到需要处理死信的队列上，并设置路由指明将死亡消息发送到哪个死信队列

   ```java
   @Bean("confirmQueue")
   public Queue confirmQueue(){
   
       Map<String, Object> map = new HashMap<>();
       map.put("x-dead-letter-exchange",DEAD_EXCHANGE_NAME); // 设置死信交换机
   	map.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY); // 设置死信路由键，一个死信交换机可能存在多个死信队列，所以要指明。
       return QueueBuilder.durable(TEST_QUEUE_NAME).withArguments(map).build();
   }
   ```

## 总结

> - 死信交换机和死信队列和普通的没有区别
>
> - 当消息成为死信后，如果该队列绑定了死信交换机，则消息会被死信交换机重新路由到死信队列
>
> - 消息成为死信的三种情况：
>
>   • 消息在队列的存活时间超过设置的生存时间（TTL)时间
>
>   • 消息队列的消息数量已经超过最大队列长度
>
>   • 消息被否定确认，使用basicNack或basicReject并且requeue=false

# 延迟队列

消息进入队列后不会立即被消费，只有到达指定时间后，才会被消费，最重要的特征就是延迟上

## **使用场景**

1. **订单超时处理**：在电子商务系统中，可以设置一个延迟队列来处理订单的超时问题。如果用户在一定时间内没有完成支付，系统可以自动取消订单。
2. **任务重试**：在分布式系统中，某些任务可能由于临时性问题（如网络故障）而失败。可以将这些任务放入延迟队列，稍后再进行重试。
3. **定时通知**：发送定时通知或提醒，例如用户订阅的服务到期提醒。

## 实现方式

### TTL + 死信队列的方式

<img src="./assets/image-20241113212656892.png" alt="image-20241113212656892" style="zoom:67%;" />

> **存在的问题**
>
> 消费不及时，不按时消费。
>
> 因为`RabbitMQ`是等消息到达队列顶部即将被消费时，才会判断其是否过期并删除或者移至死信队列。
>
> 最顶端的数据一直不过期，会导致后面的消息不能够进入死信。

### 通过延时Plugin

![image-20241113230740440](./assets/image-20241113230740440.png)

#### 安装

 **下载地址**： https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases

**上传位置** 

```sh
cd /usr/lib/rabbitmq/lib/rabbitmq_server-3.8.34/plugins
```

**启用插件**

```sh
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

安装插件后，交换机类型多出`x-delayed-message`

<img src="./assets/image-20241113221832267.png" alt="image-20241113221832267" style="zoom:50%;" />

**重启服务** 

```sh
rabbitmqctl start
```

#### 1. 创建延迟交换机

```java
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String DELAYED_EXCHANGE_NAME = "delayed_exchange";
    public static final String DELAYED_QUEUE_NAME = "delayed_queue";
    public static final String ROUTING_KEY = "delayed_routing_key";

    // 自定义交换机
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct"); // 可以选择 direct, fanout, topic 等类型
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME, true, false, false);
    }

    @Bean
    public Binding delayedBinding(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(ROUTING_KEY).noargs();
    }
}
```

#### 2. 发送延迟消息

```java
 @Test
    void plugsTest() {
        CorrelationData data = new CorrelationData("1");//设置当前的数据id为1
        MessagePostProcessor processor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties properties = message.getMessageProperties();

                properties.setDelay(5000);
                
                return message;
            }
        };
        rabbitTemplate.convertAndSend("delayed_exchange","ack","1111111",processor,data);
    }
```

# RabbitMQ实战场景

## 日志与可视化监控查看

**日志文件：**

```shell
cd /var/log/rabbitmq/rabbit@localhost.log
```

**停止服务：**

```shell
/sbin/service rabbitmq-server stop
```

**查看进程：**

```shell
ps -ef|grep rabbitmq
```

**重新启动:** (有问题)

```shell
rabbitmqctl start_app
```

**开启服务：**

```shell
/bin/systemctl start rabbitmq-server.service
```

**监控日志：**

```shell
tail -n 500 -f 
```

## **消息追踪**

`amq.rabbitmq.trace` 是 RabbitMQ 内置的一个特殊交换机，它主要用于调试目的，可以帮助开发者或运维人员追踪消息在 RabbitMQ 中的流动情况。通过启用 `amq.rabbitmq.trace`，可以捕获发送到指定队列的消息及其属性，这对于理解复杂的路由规则、诊断消息传递问题非常有帮助。

### 使用场景

1. **开发调试**：在开发阶段，可以利用 `amq.rabbitmq.trace` 来验证消息是否按照预期的路径正确传递，确保消息格式和内容符合要求。
2. **故障排查**：当生产环境中遇到消息传递异常时，启用 `amq.rabbitmq.trace` 可以帮助快速定位问题所在，例如检查消息是否被正确路由到了目标队列。
3. **性能优化**：通过分析消息流动的数据，可以识别潜在的性能瓶颈，为优化系统提供依据

### 注意事项

- **性能影响**：启用 `amq.rabbitmq.trace` 会增加系统的开销，因为它需要额外复制并处理每条消息。因此，在生产环境中应谨慎使用，仅在必要时启用，并在调试完成后关闭。
- **安全性**：由于 `amq.rabbitmq.trace` 会捕获所有消息的内容，因此可能存在泄露敏感信息的风险。确保只在受控环境中使用，并且限制访问权限。
- **数据量**：如果系统中有大量消息传递，`trace_queue` 可能会迅速积累大量的消息，导致磁盘空间不足等问题。建议定期清理或限制队列大小。

### 启用方式

1. **Firehose：生产者给交换机发送消息时，会按照指定的格式发送到amq.rabbitmq.trace（Topic）**交换机上

   **开启Firehose命令：**

   ```shell
   rabbitmqctl trace_on
   ```

   **关闭Firehose命令**

   ```shell
   rabbitmqctl trace_off
   ```

2. **使用数据可视化插件**

查看插件列表

```shell
rabbitmq-plugins list
```

开启可视化插件

```shell
rabbitmq-plugins enable rabbitmq_tracing
```

关闭可视化插件

```shell
rabbitmq-plugins disable rabbitmq_tracing
```



<img src="./assets/image-20241114195934550.png" alt="image-20241114195934550" style="zoom: 67%;" />

## 幂等性保障

**幂等性：**是分布式中比较重要的一个概念，是指在多作业操作时候避免造成重复影响，其实就是保证同一个消息不被

消费者重复消费两次。但是实际开发中可能存在网络波动等问题，生产者无法接受消费者发送的ack信息，因此这条消

息将会被重复发送给其他消费者进行消费，实际上这条消息已经被消费过了，这就是重复消费的问题。

**如何去避免重复消费问题：**

1. 数据库乐观锁机制

   ```sql
   update items set count=count-1 where count= #{count} and id = #{id}
   
   update items set count=count-1,version=version+1 where version=#{version} and id = #{id}
   ```

   > 可以省略version，通过比较更新操作前后count的值，来判断是否可以修改。

2. 生成全局唯一id+redis锁机制:操作之前先判断是否抢占了分布式锁 setNx 命名

# RabbitMQ集群

**单节点存在的问题**

- 单节点的RabbitMQ如果内存崩溃、机器掉电或者主板故障，会影响整个业务线正常使用
- 单机吞吐性能会受内存、带宽大小限制

![image-20241114204409242](./assets/image-20241114204409242.png)

**集群模式**

​	<img src="./assets/image-20241114204506764.png" alt="image-20241114204506764" style="zoom:67%;" />

**集群存在的问题**

 **高可用性问题**：单点故障导致集群不可用。

![image-20241115212820192](./assets/image-20241115212820192.png)

# **HAProxy** 

HAProxy 是一个高性能的开源负载均衡器，常用于提高应用程序的可用性和扩展性。在 RabbitMQ 集群中使用 HAProxy 可以实现客户端请求的负载均衡，提高系统的可靠性和性能。

<img src="./assets/image-20241115213440625.png" alt="image-20241115213440625" style="zoom:67%;" />
