package com.weblearnex.app.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblearnex.app.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String , Object> redisTemplate;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void save(String key, Object value) {
        try {
            String str = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, str);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
