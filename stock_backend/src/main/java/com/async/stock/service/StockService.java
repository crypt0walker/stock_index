package com.async.stock.service;

import com.async.stock.pojo.domain.*;
//import com.async.stock.pojo.domain.StockUpdownDomain;
import com.async.stock.vo.resp.PageResult;
import com.async.stock.vo.resp.R;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author : itheima
 * @date : 2022/9/20 17:33
 * @description :
 */
public interface StockService {
    /**
     * 查询最新的国内大盘信息
     * @return
     */
    R<List<InnerMarketDomain>> getInnerIndexAll();

    /**
     * 需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    R<List<StockBlockDomain>> sectorAllLimit();

    /**
     * 分页降序查询最新的个股涨幅排数据
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
    R<PageResult<StockUpdownDomain>> getPageStockInfos(Integer page, Integer pageSize);
    /**
     * 查询最新4条个股涨幅数据，按涨幅降序
     * @return
     */
    R<List<StockUpdownDomain>> getNewestStockInfos();
    /**
     * 统计最新交易日下股票在各个时间点涨跌停的数量
     * @return
     */
    R<Map<String, List>> getStockUpDownCount();

    /**
     * 导致指定页码的股票涨幅数据到excel
     * @param page
     * @param pageSize
     * @param response
     */
    void exportPageStockInfos(Integer page, Integer pageSize, HttpServletResponse response);
    /**
     * 功能描述：统计国内A股大盘T日和T-1日成交量对比功能（成交量为沪市和深市成交量之和）
     * @return
     */
    R<Map> stockTradeVol4InnerMarket();
    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     * @return
     */
    R<Map> stockUpDownScopeCount();
    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code);
    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    R<List<Stock4EvrDayDomain>> stockCreenDkLine(String stockCode);

    R<List<InnerMarketDomain>> getInnerMarketInfos();

//    R<List<InnerMarketDomain>> getInnnerMarketInfos();
}
