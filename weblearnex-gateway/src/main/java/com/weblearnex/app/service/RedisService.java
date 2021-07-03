package com.weblearnex.app.service;

public interface RedisService {
    public void save(String key, Object value);
    public Object get(String key);
}
