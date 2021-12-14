package edu.nuist.ojs.message.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import edu.nuist.ojs.message.entity.EmailServer;
import java.util.UUID;

import cn.hutool.crypto.SecureUtil;

@Service
public class RedisRouter {
    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }
    
    public void set(String key, Object obj){
        redisTemplate.opsForValue().set(key,obj);
    }
    public String saveEmail(EmailServer emailServer){
        String key= "<email."+ SecureUtil.md5( ""+ UUID.randomUUID())+"@ojs>";
        emailServer.setMessageId(key);
        redisTemplate.opsForValue().set(key,emailServer);
        return key;
    }
}
