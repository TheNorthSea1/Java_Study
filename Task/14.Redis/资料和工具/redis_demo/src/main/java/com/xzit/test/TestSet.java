package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Slf4j(topic = "c.TestSet")
public class TestSet {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        //s_add(jedis,"k1","aaa","bbb","ccc","ddd");
       // s_add(jedis,"k2","bbb","ccc","ddd","eee");
        //s_members(jedis,"k1");
        //s_ismember(jedis,"k1","aaaa");
        //scard(jedis,"k1");
        //s_rem(jedis,"k1","aaa","bbb");
        s_diff(jedis,"k2","k1");
    }
    public static void s_add(Jedis jedis,String key,String...members){
        jedis.sadd(key,members);
    }
    public static void s_members(Jedis jedis,String key){
        Set<String> set = jedis.smembers(key);
        set.forEach(System.out::println);
    }
    public static void s_ismember(Jedis jedis,String key,String member){
        boolean b = jedis.sismember(key, member);
        log.debug("{}",b);
    }
    public static void scard(Jedis jedis,String key){
        long scard = jedis.scard(key);
        log.debug("{}",scard);
    }
    public static void s_rem(Jedis jedis,String key,String...members){
        long srem = jedis.srem(key, members);
        log.debug("{}",srem);
    }
    public static void s_diff(Jedis jedis,String...keys){
        Set<String> sdiff = jedis.sdiff(keys);
        sdiff.forEach(System.out::println);
    }
}
