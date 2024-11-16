package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "c.TestHash")
public class TestHash {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        //h_set(jedis,"user:1001","name","张三");
       // h_set(jedis,"user:1001","age","20");
       // h_get(jedis,"user:1001","name");
        /*Map<String,String> map=new HashMap<>();
        map.put("name","lisi");
        map.put("age","20");
        map.put("gender","man");
        h_set(jedis,"user:1002",map);*/
       // h_getAll(jedis,"user:1002");
        h_keys(jedis,"user:1002");
    }
    public static void h_set(Jedis jedis,String key,String field,String value){
        jedis.hset(key,field,value);
    }
    public static void h_set(Jedis jedis, String key, Map<String,String> value){
        jedis.hset(key,value);
    }
    public static void h_get(Jedis jedis,String key,String field){
        String value = jedis.hget(key, field);
        log.debug(value);
    }
    public static void h_getAll(Jedis jedis,String key){
        Map<String, String> map = jedis.hgetAll(key);
        map.forEach((k,v)->log.debug(k+"\t"+v));
       /* for (String key1:map.keySet()){
            log.debug(key1+"\t"+map.get(key1));
        }*/
    }
    public static void h_keys(Jedis jedis,String key){
        Set<String> fields = jedis.hkeys(key);
        fields.forEach(System.out::println);

    }
}
