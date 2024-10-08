package com.async.stock.service;

/**
 * @author async
 * @version 1.0
 */
public interface StockTimerTaskService {
    /**
     * 采集获取大盘数据信息
     */
    void collectInnerMarketInfo();

    /**
     * 采集获取国内A股 股票详情信息
     */
    void collectAShareInfo();

    /**
     * 采集板块股票详情
     */
    void getStockSectorRtIndex();

}
