package com.ruoyi.common.core.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * spring redis 工具类
 *
 * @author ruoyi
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired(required = false)
    public RedisTemplate redisTemplate;

    // 内存缓存作为Redis的备选方案
    private final Map<String, Object> memoryCache = new ConcurrentHashMap<>();

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        if (redisTemplate != null)
        {
            redisTemplate.opsForValue().set(key, value);
        }
        else
        {
            memoryCache.put(key, value);
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        if (redisTemplate != null)
        {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        }
        else
        {
            memoryCache.put(key, value);
            // 内存缓存不支持过期时间，仅作为备选
        }
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.expire(key, timeout, unit);
        }
        else
        {
            // 内存缓存不支持过期时间
            return false;
        }
    }

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    public long getExpire(final String key)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.getExpire(key);
        }
        else
        {
            // 内存缓存不支持过期时间
            return -1;
        }
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.hasKey(key);
        }
        else
        {
            return memoryCache.containsKey(key);
        }
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key)
    {
        if (redisTemplate != null)
        {
            ValueOperations<String, T> operation = redisTemplate.opsForValue();
            return operation.get(key);
        }
        else
        {
            return (T) memoryCache.get(key);
        }
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.delete(key);
        }
        else
        {
            return memoryCache.remove(key) != null;
        }
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public boolean deleteObject(final Collection collection)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.delete(collection) > 0;
        }
        else
        {
            boolean deleted = false;
            for (Object key : collection)
            {
                if (key instanceof String)
                {
                    deleted |= memoryCache.remove(key) != null;
                }
            }
            return deleted;
        }
    }

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        if (redisTemplate != null)
        {
            Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
            return count == null ? 0 : count;
        }
        else
        {
            memoryCache.put(key, dataList);
            return dataList != null ? dataList.size() : 0;
        }
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.opsForList().range(key, 0, -1);
        }
        else
        {
            Object value = memoryCache.get(key);
            return value instanceof List ? (List<T>) value : null;
        }
    }

    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        if (redisTemplate != null)
        {
            BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
            Iterator<T> it = dataSet.iterator();
            while (it.hasNext())
            {
                setOperation.add(it.next());
            }
            return setOperation;
        }
        else
        {
            memoryCache.put(key, dataSet);
            return null; // 内存缓存不支持BoundSetOperations
        }
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.opsForSet().members(key);
        }
        else
        {
            Object value = memoryCache.get(key);
            return value instanceof Set ? (Set<T>) value : null;
        }
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (redisTemplate != null)
        {
            if (dataMap != null) {
                redisTemplate.opsForHash().putAll(key, dataMap);
            }
        }
        else
        {
            if (dataMap != null) {
                memoryCache.put(key, new HashMap<>(dataMap));
            }
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.opsForHash().entries(key);
        }
        else
        {
            Object value = memoryCache.get(key);
            return value instanceof Map ? (Map<String, T>) value : null;
        }
    }

    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        if (redisTemplate != null)
        {
            redisTemplate.opsForHash().put(key, hKey, value);
        }
        else
        {
            Object valueObj = memoryCache.get(key);
            if (valueObj instanceof Map) {
                ((Map<String, T>) valueObj).put(hKey, value);
            } else {
                Map<String, T> newMap = new HashMap<>();
                newMap.put(hKey, value);
                memoryCache.put(key, newMap);
            }
        }
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        if (redisTemplate != null)
        {
            HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
            return opsForHash.get(key, hKey);
        }
        else
        {
            Object valueObj = memoryCache.get(key);
            if (valueObj instanceof Map) {
                return ((Map<String, T>) valueObj).get(hKey);
            }
            return null;
        }
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.opsForHash().multiGet(key, hKeys);
        }
        else
        {
            List<T> result = new ArrayList<>();
            Object valueObj = memoryCache.get(key);
            if (valueObj instanceof Map) {
                Map<String, T> map = (Map<String, T>) valueObj;
                for (Object hKey : hKeys) {
                    if (hKey instanceof String) {
                        result.add(map.get(hKey));
                    } else {
                        result.add(null);
                    }
                }
            }
            return result;
        }
    }

    /**
     * 删除Hash中的某条数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    public boolean deleteCacheMapValue(final String key, final String hKey)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.opsForHash().delete(key, hKey) > 0;
        }
        else
        {
            Object valueObj = memoryCache.get(key);
            if (valueObj instanceof Map) {
                return ((Map<?, ?>) valueObj).remove(hKey) != null;
            }
            return false;
        }
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern)
    {
        if (redisTemplate != null)
        {
            return redisTemplate.keys(pattern);
        }
        else
        {
            // 简单实现，只支持精确匹配
            if (pattern != null && !pattern.contains("*")) {
                return memoryCache.containsKey(pattern) ? List.of(pattern) : Collections.emptyList();
            }
            // 支持简单的前缀匹配
            if (pattern != null && pattern.endsWith("*")) {
                String prefix = pattern.substring(0, pattern.length() - 1);
                List<String> result = new ArrayList<>();
                for (String key : memoryCache.keySet()) {
                    if (key.startsWith(prefix)) {
                        result.add(key);
                    }
                }
                return result;
            }
            return Collections.emptyList();
        }
    }
}
