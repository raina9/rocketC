package com.weblearnex.app.service;

import com.weblearnex.app.config.ProgressBean;

public interface RedisService {
    public void save(String key, Object value);
    public void save(String key, Object value, Long time);
    public Object get(String key);
    public ProgressBean getProgressBean(String key);
    public boolean delete(String key);
    public boolean keyExist(String key);
}
