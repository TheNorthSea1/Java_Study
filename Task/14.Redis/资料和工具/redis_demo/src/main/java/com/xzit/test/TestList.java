package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;

@Slf4j(topic="c.TestList")
public class TestList {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        //l_push(jedis,"k1","aaa","bbb","ccc");
        //l_range(jedis,"k1",0,-1);
        //l_pop(jedis,"k1",2);
        l_rem(jedis,"k1",2,"bbb");
    }
    public static void l_push(Jedis jedis,String key,String...values){
        jedis.lpush(key,values);
    }
    public static void l_range(Jedis jedis,String key,long start,long stop){
        List<String> list = jedis.lrange(key, start, stop);
        list.forEach(c->log.debug(c));
    }
    public static void l_pop(Jedis jedis,String key,int count){
        List<String> lpop = jedis.lpop(key, count);
        lpop.forEach(c->log.debug(c));
    }
    public static void l_rem(Jedis jedis,String key,long count,String value){
        long lrem = jedis.lrem(key, count, value);
        log.debug("成功删除{}",lrem);
    }
}
