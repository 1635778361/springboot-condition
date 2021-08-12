package com.yyzq.springbootcondition.config;

import com.yyzq.springbootcondition.condition.ConditionOnClass;
import com.yyzq.springbootcondition.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    @ConditionOnClass("redis.clients.jedis.Jedis")
    public User user(){
        return new User();
    }
}
