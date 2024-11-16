package com.xzit.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.text.DecimalFormat;
import java.util.Random;

//需求：用户使用银行APP登录，要求动态发送手机验证码，验证码设定5分钟有效，1天内最多发送3次
@Slf4j(topic="c.TestApp")
public class TestApp {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("192.168.44.4",6379);
        //send(jedis,"13012345678");
        verify(jedis,"13012345678","067096");

    }
    public static String getCode(){
        return new DecimalFormat("000000").format(new Random().nextInt(1000000));
    }
    public static void send(Jedis jedis,String phoneNumber){
        String count_key="v:"+phoneNumber+":count";//当前手机号已发送次数的key
        String code_key="v:"+phoneNumber+":code";//当前手机号已收到的验证码key
        String s=jedis.get(count_key);//获取手机号已发送的次数
        if (s==null){
            //如果没有获取，就表时该key不存在，第一次设置，有效时间为1天
            jedis.setex(count_key,60*60*24,"1");
            log.debug("发送成功");
        }else if(Integer.parseInt(s)<3){//如果不到3次，可以为用户发送，每发送一次，记数增加1
            jedis.incr(count_key);
            log.debug("发送成功");
        }else{
            log.debug("今日转帐3次，24小时后再试");
            jedis.close();
            return;
        }
        jedis.setex(code_key,60*5,getCode());//发送验证码进行保存
        jedis.close();
    }
    public static void verify(Jedis jedis,String phoneNumber,String code){
        //获取在redis中存储的手机验证码
        String code_key="v:"+phoneNumber+":code";
        String redis_code=jedis.get(code_key);
        //比较两个验证码是否相同
        log.debug(code.equals(redis_code)?"成功":"失败");
    }
    /*public static String getCode(){
        String v="0123456789";
        String code="";
        for (int i=1;i<=6;i++){
            code+=v.charAt((int)(Math.random()*v.length()));
        }
        return code;
    }*/
}
