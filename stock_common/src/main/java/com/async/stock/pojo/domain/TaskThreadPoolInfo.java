package com.async.stock.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author by async
 * @Date 2024/10/06
 * @Description
 */
@ConfigurationProperties(prefix = "task.pool")
@Data
@ApiModel("线程池实体类")
public class TaskThreadPoolInfo {
    /**
     *  核心线程数（获取硬件）：线程池创建时候初始化的线程数
     */
    @ApiModelProperty("核心线程数")
    private Integer corePoolSize;
    @ApiModelProperty("最大线程数")
    private Integer maxPoolSize;
    @ApiModelProperty("线程活跃时间")
    private Integer keepAliveSeconds;
    @ApiModelProperty("阻塞队列容量")
    private Integer queueCapacity;
}