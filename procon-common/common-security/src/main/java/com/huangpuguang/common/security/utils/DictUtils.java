package com.huangpuguang.common.security.utils;

import com.huangpuguang.common.core.constant.Constants;
import com.huangpuguang.common.core.utils.SpringUtils;
import com.huangpuguang.common.core.utils.ProconStrUtils;
import com.huangpuguang.common.redis.service.RedisService;
import com.huangpuguang.system.api.domain.SysDictData;

import java.util.Collection;
import java.util.List;

/**
 * 字典工具类
 *
 * @author procon
 */
public class DictUtils
{
    /**
     * 设置字典缓存
     *
     * @param key 参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(RedisService.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key)
    {
        Object cacheObj = SpringUtils.getBean(RedisService.class).getCacheObject(getCacheKey(key));
        if (ProconStrUtils.isNotNull(cacheObj))
        {
            return ProconStrUtils.cast(cacheObj);
        }
        return null;
    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key)
    {
        SpringUtils.getBean(RedisService.class).deleteObject(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisService.class).keys(Constants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisService.class).deleteObject(keys);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(String configKey)
    {
        return Constants.SYS_DICT_KEY + configKey;
    }
}
