package com.zhang.jwt.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.io.IOException;
import java.util.Properties;

public enum RedisUtils {
    INSTANCE;
    static JedisPool jedisPool = null;

    static {
        //1 创建连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        //2 进行配置-四个配置
        config.setMaxIdle(1);//最小连接数
        config.setMaxTotal(11);//最大连接数
        config.setMaxWaitMillis(10 * 1000L);//最长等待时间
        config.setTestOnBorrow(true);//测试连接时是否畅通
        //3 通过配置对象创建连接池对象
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(RedisUtils.class.getClassLoader().getResourceAsStream("redis.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String host = properties.getProperty("redis.host");
        String port = properties.getProperty("redis.port");
        String password = properties.getProperty("redis.password");
        String timeout = properties.getProperty("redis.timeout");
        jedisPool = new JedisPool(config, host, Integer.valueOf(port), Integer.valueOf(timeout), password);
    }

    //获取连接
    public Jedis getSource() {
        return jedisPool.getResource();
    }

    //关闭资源
    public void closeSource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 设置字符值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value,String sessionKey) {
        Jedis jedis = getSource();
        jedis.set(key, value,new SetParams());
        closeSource(jedis);
    }


    /**
     * 设置
     *
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getSource();
        jedis.set(key, value);
        closeSource(jedis);
    }

    /**
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        Jedis jedis = getSource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSource(jedis);
        }
        return null;
    }
    public String get(String key) {
        Jedis jedis = getSource();
        try {
            return jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSource(jedis);
        }
        return null;
    }
}
