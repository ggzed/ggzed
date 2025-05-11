package com.yf.config;


import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@EnableScheduling
public class ThreadPoolConfig {

    /**
     * 配置定时任务执行器
     */
    @Bean(name = "scheduledExecutorService")
    public ScheduledExecutorService scheduledExecutorService() {
        // 核心线程数为 CPU 核心数
        int corePoolSize = Runtime.getRuntime().availableProcessors();

        // 创建调度线程池
        ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(
                corePoolSize,
                new BasicThreadFactory.Builder()
                        .namingPattern("schedule-pool-%d")
                        .daemon(true)
                        .build()
        );

        // 配置线程池策略
        scheduledExecutorService.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 设置调度线程池的行为：在关闭时不继续执行已有的周期任务
        scheduledExecutorService.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        scheduledExecutorService.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);

        return scheduledExecutorService;
    }

    /**
     * 配置 CPU 密集型线程池
     * 适用场景：需要大量进行 CPU 计算的任务，如图像处理、大数据处理等。
     */
    @Bean(name = "cpuIntensiveExecutor")
    public TaskExecutor cpuIntensiveExecutor() {
        return new ThreadPoolTaskExecutor() {
            {
                setMaxPoolSize(Runtime.getRuntime().availableProcessors()); // 核心线程数设置为CPU核心数
                setCorePoolSize(Runtime.getRuntime().availableProcessors());// 最大线程数也设置为CPU核心数
                setQueueCapacity(0);                                        // 使用SynchronousQueue
                setKeepAliveSeconds(0);                                     // 空闲线程立即终止
                setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy()); // 队列满时抛出异常
            }
        };
    }

    /**
     * 配置 IO 密集型线程池
     * 适用场景：需要进行大量 IO 操作的任务，如文件读写、网络请求等。
     */
    @Bean(name = "ioIntensiveExecutor")
    public TaskExecutor ioIntensiveExecutor() {
        return new ThreadPoolTaskExecutor() {
            {
                setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2); // 核心线程数设置为CPU核心数
                setCorePoolSize(Runtime.getRuntime().availableProcessors()); // 最大线程数设置为核心数的两倍
                setQueueCapacity(1000);  // 较大的队列容量
                setKeepAliveSeconds(300); // 线程保持活动的时间，秒
                setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 队列满时由提交任务的线程自己执行该任务
            }
        };
    }
}