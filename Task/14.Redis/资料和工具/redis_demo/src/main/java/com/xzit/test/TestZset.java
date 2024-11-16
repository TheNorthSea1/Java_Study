package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j(topic = "c.TestZset")
public class TestZset {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        //z_add(jedis,"k1",100,"元旦假期");
        //z_add(jedis,"k1",110,"清明假期");
        //z_add(jedis,"k1",120,"五一假期");
       // z_range(jedis,"k1",0,-1);
       /* Map<String,Double> map=new HashMap<>();
        map.put("元旦假期",100.0);
        map.put("清明假期",110.0);
        map.put("五五假期",120.0);
        z_add(jedis,"k2",map);*/
        //z_range(jedis,"k2",0,-1);
        //z_rev_range(jedis,"k2",0,-1);
        //z_rank(jedis,"k1","清明假期");
        z_card(jedis,"k1");
    }
    public static void z_add(Jedis jedis,String key,double score,String member){
        jedis.zadd(key,score,member);
    }
    public static void z_add(Jedis jedis, String key, Map<String,Double> members){
        jedis.zadd(key,members);
    }
    public static void z_range(Jedis jedis,String key,long start,long stop){
        List<String> list = jedis.zrange(key, start, stop);
        list.forEach(System.out::println);
    }
    public static void z_rev_range(Jedis jedis,String key,long start,long stop){
        List<String> list = jedis.zrevrange(key, start, stop);
        list.forEach(System.out::println);
    }
    public static void z_rank(Jedis jedis,String key,String member){
        Long index = jedis.zrank(key, member);
        log.debug("{}",index);
    }
    public static void z_card(Jedis jedis,String key){
        long zcard = jedis.zcard(key);
        log.debug("{}",zcard);
    }

}
