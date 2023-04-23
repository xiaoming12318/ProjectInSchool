package com.exampl.aaa;

import com.exampl.common.SendSms;
import com.exampl.util.ValidateCodeUtils;
import org.junit.jupiter.api.Test;
import org.omg.CORBA.TIMEOUT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;

import java.util.Set;
import java.util.concurrent.ExecutionException;

@SpringBootTest
public class text {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private JedisPool jedisPool;


    @Test
    public void  redisTest(){
        redisTemplate.opsForSet().add("01","王五");
        Object o = redisTemplate.boundValueOps("01");

        System.out.println(o);
    }


    @Test
    public void ssm() throws ExecutionException, InterruptedException {
        SendSms sendSms=new SendSms();
        System.out.println(jedisPool.getResource().get("555"));


    }
}
