package com.weblearnex.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblearnex.app.config.ProgressBean;
import com.weblearnex.app.entity.setup.User;
import com.weblearnex.app.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String , Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void save(String key, Object value) {
        //User user = objectMapper.readValue(objectMapper.writeValueAsString(userObject), User.class);
        // redisTemplate.opsForValue().se
        try{
            String str = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, str);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void save(String key, Object value, Long time) {
        try{
            String str = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, str, time, TimeUnit.SECONDS);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public Object get(String key) {
        if(redisTemplate.hasKey(key)){
            try {
                return redisTemplate.opsForValue().get(key);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ProgressBean getProgressBean(String key) {
        if(redisTemplate.hasKey(key)){
            try {
                String strObj = (String) redisTemplate.opsForValue().get(key);
                return objectMapper.readValue(strObj, ProgressBean.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean delete(String key) {
        if(redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
        return true;
    }

    @Override
    public boolean keyExist(String key) {
        if(key == null || key.trim().equals("")){
            return false;
        }
        return redisTemplate.hasKey(key);
    }
}
