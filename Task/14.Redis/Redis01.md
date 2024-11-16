# 1. redis概述

## **1.1** 简介

- 数据库排名https://db-engines.com/en/ranking
- redis(Remote Dictionary Server) 一个开源的key-value存储系统
- 它支持存储的Value类型：包括String(字符串),list(链表),set(集合),zset(sorted set 有序集合),hash(哈希类型)。都支持push/pop、add/romove，获取交集、并集、差集等一些相关操作，操作是原子性的。
- redis支持各种不同方式的排序
- redis (与memcatched相同)数据存在内存中
- redis会周期性的把更新的数据写入磁盘，或者把修改的操作追加到记录文件
- redis支持集群，实现master-slave(主从)同步操作

## **1.2** 应用场景

- 缓存：配合关系型数据库做高速缓存
- 计数器：进行自增自减运算
- 时效性数据：利用expire过期，例如手机验证码功能
- 海量数据统计：利用位图，存储用户是否是会员、日活统计、文章已读统计、是否参加过某次活动
- 会话缓存：使用redis统一存储多台服务器用到的session信息
- 分布式队列/阻塞队列：通过List双向链表实现读取和阻塞队列
- 分布式锁: 使用redis自带setnx命令实现分布式锁
- 热点数据存储：最新文章、最新评论，可以使用redis的list存储，ltrim取出热点数据，删除旧数据
- 社交系统：通过Set功能实现，交集、并集实现获取共同好友，差集实现好友推荐，文章推荐
- 排行榜：利用sorted-set的有序性，实现排行榜功能，取top n
- 延迟队列：利用消费者和生产者模式实现延迟队列
- 去重复数据：利用Set集合，去除大量重复数据
- 发布/订阅消息：pub/sub模式

# 2.redis使用的基本命令

1. **默认16个数据库，类似数组下标从0开始，初始默认使用0号库。使用select 命令进行切换。**

```shell
select < dbid>
select 1
```

2. **通过key获取value**

   ```shell
   get <key>
   ```

3. **统一密码管理 ，所有库使用同样的密码**

3. **查看当前数据库的key的数量**

   ```shell
   dbsize
   ```

4.  **清空当前库**

   ```shell
   flushdb
   ```

5. **清空全部库**

   ```shell
   flushall 
   ```

6. **查看当前库所有key**

   ```shell
   keys *
   ```

7. **判断某个key是否存在**

   ```shell
   exists < key>
   ```

8.  **相看key的类型**

   ```shell
   type < key>
   ```

9.  **查看底层数据类型**

   ```shell
   object encoding < key>
   ```

10. **删除指定的key数据**

    ```shell
    del < key>
    ```

11. **根据选择非阻塞删除。仅将key从keyspace元数据中删除，真正的删除会在后续中做异步操作**

    ```shell
    unlink < key> 
    ```

12. **为给定的key设置过期时间，以秒为单位**

    ```shell
    expire < key>< seconds>：
    ```

13. **查看给定key的过期时间：-1表示永不过期 -2 表示已过期**

    ```shell
    ttl < key>：
    ```

# 3.redis常用的五种数据类型

## 3.1 Redis String字符串

### 简介

- String类型在redis中最常见的一种类型
- string类型是二制安全的，可以存放字符串、数值、json、图像数据
- value存储最大数据量是512M

### 常用命令

- **set** < key>< value>：添加键值对

  - nx：当数据库中key**不存在时**，可以将key-value添加到数据库

    ```shell
    set key value nx
    ```

  - xx: 当数据库key**存在时**，可以将key-value添加到数据库，与nx参数互斥

  - ex: 设置key-value添加到数据库，并设置key的超时时间(以秒钟为单位)

    ```shell
    set key value ex 10
    ```

  - px:设置key-value添加到数据库，并设置key的超时时间(以豪秒钟为单位)，与ex互斥

- **get** < key>查询对应键值

- **append** < key>< value>：将给定的值追加到key的末尾

- **strlen** < key>：获取值的长度

- **setnx** < key>< value>：只有在key不存在时，设置key-value加入到数据库

- **setex** < key> < timeout>< value>：添加键值对，同时设置过期时间(以秒为单位)

- **incr** < key>：将key中存储的数字加1处理，只能对数字值操作。如果是空，值为1

- **decr** < key>：将key中存储的数字减1处理，只能对数字值操作。如果是空，值为1

- **incrby** < key>< increment>：将key中存储的数字值增加指定步长的数值,如果是空，值为步长。(具有原子性)

  > `原子性`:
  >
  > 这意味着在执行命令时，Redis 会保证整个操作在一个步骤内完成，不会被其他命令中断。这种原子性确保了在高并发环境下，多个客户端同时对同一个键进行增量操作时，结果是正确且一致的。

- **decrby** < key>< decrement>: 将key中存储的数字值减少指定步长的数值,如果是空，值为步长。(具有原子性)

- **mset** < key1>< value1>[< key2>< value2>...]：同时设置1个或多个key-value值

- **mget** < key1>[< key2>...]：同时获取1个或多个value

- **msetnx** < key1>< value1>[< key2>< value2>...]：当所有给定的key都不存在时，同时设置1个或多个key-value值(具有原子性)

- **getrange**/**substr** < key>< start>< end> 将给定key，获取从start(包含)到end(包含)的值

- **setrange** < key>< offset>< value>：从偏移量offset开始，用value去覆盖key中存储的字符串值

- **getset**< key>< value>： 对给定的key设置新值，同时返回旧值。如果key不存在，则添加一个key-value值

### 应用场景

## 3.2 Redis List列表

### 简介

- Redis列表是简单的字符串列表，单键多值。按照插入顺序排序。可以添加一个元素到列表的头部(左边)或者尾部(右边)
- 一个列表最多可以包含2^32-1个元素
- 底层是一个**双向链表**，对两端的操作性能很高，通过索引下标的操作中间的节点性能会较差

### 常用命令

- **lpush** < key> < value1>[< value2>...]：从左侧插入一个或多个值
- **lpushx** < key> < value1>[< value2>...]：将一个或多个值插入到已存在的列表头部
- **lrange** < key>< start>< stop>：获取列表指定范围内的元素，0左边第1位，-1右边第1 位，0 ~-1取出所有
- **rpush** < key> < value1>[< value2>...]：从右侧插入一个或多个值
- **rpushx** < key> < value1>[< value2>...]：将一个或多个值插入到已存在的列表尾部
- **lpop** < key>[count]：移除并获取列表中左边第1个元素，count表明获取的总数量,返回的为移除的元素
- **rpop** < key>[count]：移除并获取列表中右边第1个元素，count表明获取的总数量,返回的为移除的元素
- **rpoplpush** < source>< destination>：移除源列表的尾部的元素(右边第一个)，将该元素添加到目标列表的头部(左边第一个)，并返回
- **lindex** < key>< index>:通过索引获取列表中的元素
- **llen** < key>：获取列表长度
- **linsert** < key> before|after < pivot>< element>：在< pivot>基准元素前或者后面插入<element>，如果key不存在，返回0。如果< pivot>不存在，返回-1，如果操作成功，返回执行后的列表长度
- **lrem** < key>< count>< element>：根据count的值，移除列表中与参数相等的元素
- **count**=0 移除表中所有与参数相等的值
- **count**>0 从表头开始向表尾搜索，移除与参数相等的元素，数量为count
- **count**<0 从表尾开始向表头搜索，移除与参数相等的元素，数量为count的绝对值
- **lset** < key>< index> < element>：设置给定索引位置的值ltrim< key>< start> < stop>：对列表进行修剪，只保留给定区间的元素，不在指定区间的被删除
- **brpop** < key> timeout：阻塞式移除指定key的元素，如果key中没有元素，就等待，直到有元素或超时，执行结束。

### 应用场景

- 数据队列
  - 堆栈stack=lpush+lpop
  - 队列queue=lpush+rpop
  - 阻塞式消息队列 blocking mq=lpush+brpop
- 订阅号时间线
  - lrange key start stop

## 3.3 Redis Hash 哈希

### 简介

- 是一个string类型的键和value（对象），特别适合于存储对象，类似于java里面学习的Map<String,Object>。

- 假设场景：需要在redis中存储学生对象,对象属性包括(id,name,gender,age)，有以下几种处理方式

  - 方式一：用key存储学生id，用value存储序列化之后用户对象(如果用户属性数据需要修改，操作较复杂，开销较大)

  - 方式二：用key存储学生id+属性名，用value存储属性值（用户id数据冗余）

  - 方式三：用key存储学生id，用value存储field+value的hash。通过key(学生d)+field(属性)可以操作对应数据。

### 常用命令

- **hset** < key>< field>< value>[< field>< value>...]：用于为哈希表中的字段赋值，如果字段在hash表中存在，则会被覆盖
- **hmset**:用法同hset，在redis4.0.0中被弃用
- **hsetnx** < key>< field>< value>：只有在字段不存在时，才设置哈希表字段中的值
- **hget** < key>< field> 返回哈希表中指定的字段的值
- **hmget** < key>< field>[< field>...]：获取哈希表中所有给定的字段值
- **hgetall** < key>：获取在哈希表中指定key的所有字段和值
- **hexists** < key>< field>：判断哈希表中指定的字段是否存在，存在返回1 ，否则返回0
- **hkeys** < key>：获取哈希表中所有的字段
- **hvals** < key>：获取哈希表中所有的值
- **hlen** < key>：获取哈希表中的field数量
- **hdel** < key>< field>[< field>...]：删除一个或多个哈希表字段
- **hincrby** < key>< field>< increment>：为哈希表key中指定的field字段的**整数**值加上增加increment
- **hincrbyfloat** < key>< field>< increment>：为哈希表key中指定的field字段的**浮点数**值加上增加increment

### 应用场景

1. 对象缓存 hset stu:001 name zhangsan age 20 gender man

2. 电商购物车操作

   - 以用户id作为key, 以商品id作为field，以商品数量作为value

   - 添加商品: hset user:001 s:001 1

   -  hset user:001 s:002 2

   - 增减商品数量:hincrby user:001 s:001 3

   - 查看购物车商品总数: hlen user:001

   - 删除商品 : hdel user:001 s:001

   - 获取所有商品： hgetall user:001

## 3.4 Redis Set集合

### 简介

set是string类型元素无序集合。对外提供的功能和list类似，是一个列表功能。集合成员是唯一的。

### 常用命令

- **sadd** < key>< member>[< member>...]：将一个或多个成员元素加入到集合中，如果集合中已经包含成员元素，则被忽略
- **smembers** < key>：返回集合中的所有成员。
- **sismember** < key>< member>：判断给定的成员元素是否是集合中的成员，如果是返回1,否则返回0
- **scard** < key>：返回集合中元素个数
- **srem** < key>< member>[< member>...]：移除集合中一个或多个元素
- **spop** < key>[< count>]：移除并返回集合中的一个或count个随机元素
- **srandmember** < key>[< count>]：与spop相似，返回随机元素，不做移除
- **smove** < source> < destination> < member>：将member元素从source源移动到destination目标
- **sinter** < key>[< key>...]：返回给定集合的交集(共同包含)元素
- **sinterstore** < destination> < key1>[< key2>...]：返回给定所有集合的交集，并存储到destination目标中
- **sunion** < key>[< key>...]：返回给定集合的并集(所有)元素
- **sunionstore** < destination> < key1>[< key2>...]：返回给定所有集合的并集，并存储到destination目标中
- **sdiff** < key>[< key>...]：返回给定集合的差集(key1中不包含key2中的元素)
- **sdiffstore** < destination> < key1>[< key2>...]：返回给定所有集合的差集，并存储到destination目标中

### 应用场景

## 3.5 Redis zset有序集合

### 简介

- 有序集合是string类型元素的集合，不允许重复出现成员
- 每个元素会关联一个double类型的分数，redis是通过分数为集合中的成员进行从小到大的排序
- 有序集合的成员是唯一的，但是分数可以重复
- 成员因为有序，可以根据分数或者次序来快速获取一个范围内的元素

### 常用命令

- **zadd** < key> < score>< member>[< score>< member>...]：将一个或多个元素及其分数加入到有序集合中
- **zrange** < key>< min>< max> [byscore|bylex] [rev] [ limit offset count] [withscores]：返回有序集合指定区间的成员，（byscore按分数区间，bylex按字典区间，rev 反向排序(分数大的写前边，小的写后边)，limit分页(offset偏移量，count返回的总数)，withscores返回时带有对应的分数)
- **zrevrange** < key>< start>< stop>[ limit offset count]：返回集合反转后的成员
- **zrangebyscore** < key>< min>< max> [withscores] [ limit offset count]：参考zrange用法
- **zrevrangebyscore**< key>< max>< min> [withscores] [ limit offset count]：参考zrange用法
- **zrangebylex** < key>< min>< max>] [ limit offset count]：通过字典区间返回有序集合的成员
- **zrangebylex** k2 - +：减号最小值,加号最大值
- **zrangebylex** k2 [aa (ac：[ 中括号表示包含给定值，( 小括号表示不包含给定值
- **zcard** < key>：获取集合中的成员数量
- **zincrby** < key> < increment>< member> ：为集合中指定成员分数加上增量increment
- **zrem** < key> < member>[< member>...]：移除集合的一个或多个成员
- **zcount** < key>< min>< max>：统计集合中指定区间分数(都包含)的成员数量
- **zrank** < key>< member>：获取集合中成员的索引位置
- **zscore** < key>< member>：获取集合中成员的分数值

### 应用场景

1. 按时间先后顺序排序：朋友圈点赞 zadd 1656667779666

2. 热搜 微博 今日头条 快手 

3. 获取topN zrevrange k1 300 10 limit 0 10