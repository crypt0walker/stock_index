package com.async.stock.config;

import com.async.stock.pojo.vo.TaskThreadPoolInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类，用于异步任务的执行
 */
@Configuration
@EnableConfigurationProperties(TaskThreadPoolInfo.class)
@Slf4j
public class TaskExecutePool {
    private final TaskThreadPoolInfo info;

    public TaskExecutePool(TaskThreadPoolInfo info) {
        this.info = info;
    }

    /**
     * 定义任务执行器
     * @return ThreadPoolTaskExecutor 线程池执行器
     */
    @Bean(name = "threadPoolTaskExecutor", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(info.getCorePoolSize()); // 核心线程数
        taskExecutor.setMaxPoolSize(info.getMaxPoolSize()); // 最大线程数
        taskExecutor.setQueueCapacity(info.getQueueCapacity()); // 队列大小
        taskExecutor.setKeepAliveSeconds(info.getKeepAliveSeconds()); // 允许的空闲时间
        taskExecutor.setThreadNamePrefix("StockThread-"); // 线程名称前缀

        // 设置拒绝策略
        taskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // 自定义处理逻辑
                log.info("任务被拒绝。不再接受新任务，将其加入到其他队列或等待");
                if (!executor.isShutdown()) {
                    try {
                        // 阻塞等待直到有空余线程
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.error("等待插入工作队列被中断", e);
                    }
                }
            }
        });

        taskExecutor.initialize(); // 初始化线程池
        return taskExecutor;
    }
}
