package com.exercise.chatting02.springconfig.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ChatMessageCacheConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactoryDb02() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setDatabase(2); // 2번 DB 설정
        return new LettuceConnectionFactory(config);
    }
    @Bean
    public RedisTemplate<String, Object> chatMessageRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryDb02());

        // Key를 문자열(String)로 저장하도록 설정, Redis는 기본적으로 모든 Key를 문자열로 저장
        template.setKeySerializer(new StringRedisSerializer());
        /*
            Value를 JSON 형식으로 직렬화하도록 설정
            객체를 JSON형식으로 직렬화하여 Redis에 저장하고, 가져올 때 다시 객체로 변환(역직렬화)하는 역할
        */
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Redis Hash 구조의 Key를 문자열(String)로 저장하도록 설정
        template.setHashKeySerializer(new StringRedisSerializer());
        /*
            Hash의 Value를 JSON으로 직렬화하여 저장
            Hash 구조를 사용할 때, Value가 단순한 문자열이 아니라 객체일 경우 JSON 직렬화가 필요
            객체를 Redis에 저장하고 다시 가져올 때 자동으로 역직렬화할 수 있다.
         */
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
