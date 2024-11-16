package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.List;

@Slf4j(topic="c.TestGeo")
public class TestGeo {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        //geo_add(jedis,"nearby",116.511023,39.945711,"person1");
        //geo_add(jedis,"nearby",116.508257,39.946735,"person2");
        //geo_dist(jedis,"nearby","person1","person2");
        //geo_radius(jedis,"nearby",116.511023,39.945711,500);
        geo_pos(jedis,"nearby","person1","person2");
    }
    public static void geo_add(Jedis jedis,String key,double longitude,double latitude,String member){
        jedis.geoadd(key,longitude,latitude,member);
    }
    public static void geo_dist(Jedis jedis,String key,String member1,String member2){
        Double d = jedis.geodist(key, member1, member2);
        log.debug("{}",d);
    }
    public static void geo_pos(Jedis jedis,String key,String...members){
        List<GeoCoordinate> list = jedis.geopos(key, members);
        list.forEach(g->{
            log.debug(g.getLongitude()+","+g.getLatitude());
        });
    }
    public static void geo_radius(Jedis jedis,String key,double longitude,double latitude,double radius){
        List<GeoRadiusResponse> list = jedis.georadius(key, longitude, latitude, radius, GeoUnit.M);
        list.forEach(g->{
            log.debug(g.getMemberByString());
        });
    }
}
