package com.async.stock.service;

/**
 * @author by async
 * @Description 定义采集股票数据的定时任务的服务接口
 */
public interface StockTimerTaskService {
    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();

    /**
     * 定义获取分钟级股票数据
     */
    void getStockRtIndex();

    /**
     * 获取板块数据采集功能
     */
    void getStockSectorRtIndex();

    /**
     * 获取国外大盘的实时数据
     */
    void getOuterMarketInfo();
}