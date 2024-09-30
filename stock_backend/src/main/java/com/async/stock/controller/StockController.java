package com.async.stock.controller;

import com.async.stock.pojo.domain.*;
//import com.async.stock.pojo.domain.StockUpdownDomain;
import com.async.stock.service.StockService;
import com.async.stock.vo.resp.PageResult;
import com.async.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author : async
 * @date : 2024/9/25 17:28
 * @description : 定义关于股票数据查询的接口bean
 */
@RestController
@RequestMapping("/api/quot")
@Api(tags = "股票查询相关接口")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 查询最新的国内大盘信息
     *
     * @return
     */
    @ApiOperation("查询最新的国内大盘信息")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerIndexAll() {
        return stockService.getInnerIndexAll();
    }

    /**
     *需求说明: 获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据
     * @return
     */
    @ApiOperation("获取沪深两市板块最新数据，以交易总金额降序查询，取前10条数据")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> sectorAll(){
        return stockService.sectorAllLimit();
    }

    /**
     * 分页查询股票最新数据，并按照涨幅排序查询
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation("分页降序查询最新的个股涨幅排数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = false, dataType = "Integer", paramType = "query")
    })
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getPageStockInfos(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return stockService.getPageStockInfos(page, pageSize);
    }

    @ApiOperation("按照涨幅查询最新的10条个股涨幅数据")
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getNewestStockInfos() {
        return stockService.getNewestStockInfos();
    }

    @ApiOperation("统计最新交易日下股票在各个时间点涨跌停的数量")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpDownCount(){
        return stockService.getStockUpDownCount();
    }



    @ApiOperation("导出指定页码的股票涨幅数据到Excel中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = false, dataType = "Integer", paramType = "query")
    })
    @GetMapping("/stock/export")
    public void exportPageStockInfos(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            HttpServletResponse response) {
        stockService.exportPageStockInfos(page,pageSize,response);
    }

    @ApiOperation("导出指定页码的股票涨幅数据到Excel中")
    @GetMapping("/stock/tradeAmt")
    public R<Map> stockTradeVol4InnerMarket(){
        return stockService.stockTradeVol4InnerMarket();
    }

    @ApiOperation(("查询当前时间下股票的涨跌幅度区间统计功能"))
    @GetMapping("/stock/updown")
    public R<Map> getStockUpDown(){
        return stockService.stockUpDownScopeCount();
    }
    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    @ApiOperation(("查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；"))
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code){
        return stockService.stockScreenTimeSharing(code);
    }
    /**
     * 单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    @ApiOperation(("单个个股日K 数据查询 ，可以根据时间区间查询数日的K线数据；"))
    @RequestMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getDayKLinData(@RequestParam("code") String stockCode){
        return stockService.stockCreenDkLine(stockCode);
    }

}
