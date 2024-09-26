package com.async.stock.service;

import com.async.stock.pojo.domain.InnerMarketDomain;
//import com.async.stock.pojo.domain.StockUpdownDomain;
import com.async.stock.pojo.domain.StockBlockDomain;
import com.async.stock.pojo.domain.StockUpdownDomain;
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
}
