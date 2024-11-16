package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;

@Slf4j(topic = "c.TestString")
public class TestString {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        //set(jedis,"k2","v2");//设置
        //log.debug(get(jedis,"k2"));//获取
        //m_set(jedis,"k3","v3","k4","v4");
       // m_get(jedis,"k2","k3");
        //set_ex(jedis,"k5",30,"v5");
        ttl(jedis,"k5");
    }
    public static void set(Jedis jedis, String key, String value){
        jedis.set(key,value);
    }
    public static String get(Jedis jedis, String key){
        return jedis.get(key);
    }
    public static void m_set(Jedis jedis,String...keysvalues){
        jedis.mset(keysvalues);
    }
    public static void m_get(Jedis jedis,String...keys){
        List<String> list = jedis.mget(keys);
        list.forEach(System.out::println);
       /* for (String s : list) {
            System.out.println(s);
        }*/
    }
    public static void set_ex(Jedis jedis,String key,long seconds,String value){
        jedis.setex(key,seconds,value);
    }
    public static void ttl(Jedis jedis,String key){
        long ttl = jedis.ttl(key);
        log.debug("{}",ttl);
    }
}
