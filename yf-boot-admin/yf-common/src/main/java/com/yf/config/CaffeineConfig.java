package com.yf.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 配置
 *
 * @author YiFei
 * @since 2024/4/29 18:53
 */
@Configuration
public class CaffeineConfig {
    /**
     * 通用数据存储
     */
    @Bean
    public Cache<String, Object> commonCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES) // 写入后10分钟过期
                .maximumSize(1000) // 最多1000条数据
                .build();
    }

    /**
     * 短暂数据缓存
     */
    @Bean
    public Cache<String, String> shortLivedCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(90, TimeUnit.SECONDS) // 写入后90秒过期
                .maximumSize(500) // 最多500条数据
                .build();
    }
//    /**
//     *  需要定时刷新数据的缓存配置
//     */
//    @Bean
//    public LoadingCache<String, StockPrice> stockPriceCache(StockService stockService) {
//        return Caffeine.newBuilder()
//                .refreshAfterWrite(1, TimeUnit.MINUTES) // 写入1分钟后刷新
//                .maximumSize(100) // 最多100条数据
//                .build(key -> stockService.getStockPrice(key)); // 使用 StockService 来加载数据
//    }
}
