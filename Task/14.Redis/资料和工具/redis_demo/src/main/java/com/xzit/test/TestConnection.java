package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j(topic = "c.TestConnection")
public class TestConnection {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        String p = jedis.ping();
        log.debug(p);
    }
}
