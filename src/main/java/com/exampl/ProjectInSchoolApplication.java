package com.exampl;

import org.apache.commons.pool2.impl.BaseObjectPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootApplication
@MapperScan("com.exampl.mapper")
public class ProjectInSchoolApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectInSchoolApplication.class, args);
    }


    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
        jedisPoolConfig.setMaxWaitMillis(BaseObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS);
        jedisPoolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MIN_IDLE);
        JedisPool jedisPool=new JedisPool(jedisPoolConfig,"192.168.26.131",6379);
        return jedisPool;
    }
}
