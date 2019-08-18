package com.arvin.common;

import com.arvin.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * create by Arvin Meng
 * Date: 2019/8/7.
 * redis连接池
 */
public class RedisPool {
    //static 保证项目启动时，redis就加载出来  写一个静态代码块用来初始它
    private static JedisPool pool;//Jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//最大连接数
    private static Integer maxIdle =  Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10"));//在jedispool中最大的idel(空闲的)jedis实例个数
    private static Integer minIdle =  Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2"));//在jedispool中最小的idel(空闲的)jedis实例个数
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("reids.test.borrow","true"));//在borrow一个jedis实例的时候，是否要要进行验证操作，如果赋值true，则得到的jedis实例肯定是可以用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在return一个jedis实例的时候，是否要要进行验证操作，如果赋值true，则放回的jedispool的jedis实例肯定是可以用的

    private static String redisIp = PropertiesUtil.getProperty("redis.ip","127.0.0.1");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port","6379"));


    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时，默认为true

        pool = new JedisPool(config,redisIp,redisPort,1000*2);
        System.out.println(redisIp);
    }

    static{
        initPool();
    }
    public static Jedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokeResource(Jedis jedis){
            pool.returnBrokenResource(jedis);
    }


    public static void returnResource(Jedis jedis){
           pool.returnResource(jedis);

    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("arvinkey","arvinValue");
        returnResource(jedis);

        pool.destroy();//临时调用,销毁连接池中的所有连接
        System.out.println("program is end");
    }

}
