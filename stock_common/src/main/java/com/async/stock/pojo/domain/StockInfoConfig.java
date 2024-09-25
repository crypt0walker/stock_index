package com.async.stock.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于配置和存储股市信息的类，包括A股和外盘的ID集合。（application-stock.yml中的对象属性）
 */
//固定前缀
@ConfigurationProperties(prefix = "stock")
//@Component //直接加入IOC容器，但是不是每个工程都会用到，所以不太合适。
//所以选择在CommonConfig中开启对改对象的加载：@EnableConfigurationProperties(StockInfoConfig.class)
@Data
@ApiModel(description = "存储从配置文件加载的股市信息")
public class StockInfoConfig {
    @ApiModelProperty(value = "A股大盘的ID集合", notes = "存储所有A股市场的主要股票ID")
    private List<String> inner; // A股大盘ID集合

    @ApiModelProperty(value = "外盘ID集合", notes = "存储所有国外市场的主要股票ID")
    private List<String> outer; // 外盘ID集合
}
