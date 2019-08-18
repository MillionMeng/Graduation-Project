package com.arvin.util;

/**
 * create by Arvin Meng
 * Date: 2019/8/8.
 */

import com.arvin.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.set(key,value);
        } catch(Exception e){
            e.printStackTrace();
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokeResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch(Exception e){
            e.printStackTrace();
            log.error("get key:{} error",key,e);
            RedisPool.returnBrokeResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是秒
    public static String setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        } catch(Exception e){
            e.printStackTrace();
            log.error("setex key:{} value:{} error",key,value,e);
            RedisPool.returnBrokeResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置key的有效期，单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch(Exception e){
            e.printStackTrace();
            log.error("expire key:{} error",key,e);
            RedisPool.returnBrokeResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch(Exception e){
            e.printStackTrace();
            log.error("del key:{} error",key,e);
            RedisPool.returnBrokeResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        RedisPoolUtil.set("keyTest","valueTest");

        String value = RedisPoolUtil.get("keyTest");

        RedisPoolUtil.setEx("keyex","valueex",60*10);

        RedisPoolUtil.expire("keyTest",60*20);

        RedisPoolUtil.del("keyTest");

        System.out.println("end");
    }

}
