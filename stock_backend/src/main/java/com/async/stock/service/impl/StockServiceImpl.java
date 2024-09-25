package com.async.stock.service.impl;

//import com.alibaba.excel.EasyExcel;
import com.async.stock.mapper.StockBlockRtInfoMapper;
import com.async.stock.mapper.StockMarketIndexInfoMapper;
import com.async.stock.mapper.StockRtInfoMapper;
import com.async.stock.pojo.domain.InnerMarketDomain;
import com.async.stock.pojo.domain.StockBlockDomain;
import com.async.stock.pojo.domain.StockInfoConfig;
import com.async.stock.service.StockService;
import com.async.stock.utils.DateTimeUtil;
import com.async.stock.vo.resp.R;
import com.async.stock.vo.resp.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : itheima
 * @date : 2022/9/20 17:34
 * @description : 定义股票查询实现
 */
@Service
@Slf4j
public class StockServiceImpl implements StockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;

//    @Autowired
//    private StockRtInfoMapper stockRtInfoMapper;

    @Override
    public R<List<InnerMarketDomain>> getInnerIndexAll() {
        //1.获取国内A股大盘的id集合
        List<String> inners = stockInfoConfig.getInner();
        //2.获取最近股票交易日期
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        lastDate=DateTime.parse("2022-01-02 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //3.将获取的java Date传入接口
        List<InnerMarketDomain> list= stockMarketIndexInfoMapper.getMarketInfo(inners,lastDate);
        //4.返回查询结果
        return R.ok(list);
    }

    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    /**
     *需求说明: 沪深两市板块分时行情数据查询，以交易时间和交易总金额降序查询，取前10条数据
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> sectorAllLimit() {

        //获取股票最新交易时间点
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO mock数据,后续删除
        lastDate=DateTime.parse("2021-12-21 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.调用mapper接口获取数据
        List<StockBlockDomain> infos=stockBlockRtInfoMapper.sectorAllLimit(lastDate);
        //2.组装数据
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA.getMessage());
        }
        return R.ok(infos);
    }

    /**
     * 分页降序查询最新的个股涨幅排数据
     * @param page 当前页
     * @param pageSize 每页大小
     * @return
     */
//    @Override
//    public R<PageResult<StockUpdownDomain>> getPageStockInfos(Integer page, Integer pageSize) {
//        //1.获取最新的股票交易时间
//        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
//        //TODO 伪造数据，后续删除
//        lastDate=DateTime.parse("2022-07-07 14:43:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        //2.设置分页参数
//        PageHelper.startPage(page,pageSize);
//        //3.调用mapper查询数据
//        List<StockUpdownDomain> infos=stockRtInfoMapper.getStockUpDownInfos(lastDate);
//        //判断数据是否为空
//        if (CollectionUtils.isEmpty(infos)) {
//            return R.error(ResponseCode.NO_RESPONSE_DATA);
//        }
//        //3.组装数据
//        //转化成PageInfo对象
//        //PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(infos);
//        PageResult<StockUpdownDomain> pageResult = new PageResult<>(new PageInfo<>(infos));
//        //4.响应数据
//        return R.ok(pageResult);
//    }

    /**
     * 统计最新交易日下股票在各个时间点涨跌停的数量
     * @return
     */
//    @Override
//    public R<Map<String, List>> getStockUpDownCount() {
//        //1.获取最新的股票交易时间范围：开盘到最新交易时间点
//        //统计最新交易时间，就是先获取最新交易时间点，然后再根据这个交易时间点获取开盘时间和收盘时间
//        //1.1 获取最新交易时间点，就是截止截止时间
//        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
//        Date endTime = endDateTime.toDate();
//        //假数据 todo
//        endTime=DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        //1.2 获取开始时间
//        Date startTime = DateTimeUtil.getOpenDate(endDateTime).toDate();
//        //TODO 假数据
//        startTime=DateTime.parse("2022-01-06 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        //2.定义flag标识 1：涨停 0：跌停
//        //3.分别统计涨停和跌停数据的集合
//        //3.1 统计涨停
//        List<Map> upList=stockRtInfoMapper.getStockUpDownCount(startTime,endTime,1);
//        //3.2 统计跌停
//        List<Map> downList=stockRtInfoMapper.getStockUpDownCount(startTime,endTime,0);
//        //4.组装数据
//        Map<String,List> infos=new HashMap();
//        infos.put("upList",upList);
//        infos.put("downList",downList);
//        //5.响应
//        return R.ok(infos);
//    }

    /**
     * 导致指定页码的股票涨幅数据到excel
     * @param page 当前页
     * @param pageSize 每页大小
     * @param response
     */
//    @Override
//    public void exportPageStockInfos(Integer page, Integer pageSize, HttpServletResponse response) {
//        //1.获取分页数据
//        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
//        //TODO 伪造数据，后续删除
//        lastDate=DateTime.parse("2022-07-07 14:43:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
//        PageHelper.startPage(page,pageSize);
//        List<StockUpdownDomain> infos=stockRtInfoMapper.getStockUpDownInfos(lastDate);
//        response.setCharacterEncoding("utf-8");
//        try {
//            //2.判断分页数据是否为空，为空则响应json格式的提示信息
//            if (CollectionUtils.isEmpty(infos)) {
//                R<Object> error = R.error(ResponseCode.NO_RESPONSE_DATA);
//                //将error转化成json格式字符串
//                String jsonData = new ObjectMapper().writeValueAsString(error);
//                //设置响应的数据格式 告知浏览器传入的数据格式
//                response.setContentType("application/json");
//                //设置编码格式
//    //            response.setCharacterEncoding("utf-8");
//                //响应数据
//                response.getWriter().write(jsonData);
//                return;
//            }
//            //3.调动EasyExcel数据导出
//            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
//            response.setContentType("application/vnd.ms-excel");
////        response.setCharacterEncoding("utf-8");
//            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//            String fileName = URLEncoder.encode("股票涨幅数据表格", "UTF-8");
//            //指定excel导出时默认的文件名称，说白了就是告诉浏览器下载文件时默认的名称为：股票涨幅数据表格
//            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨幅信息").doWrite(infos);
//        } catch (Exception e) {
//            log.error("导出时间：{},当初页码：{}，导出数据量：{}，发生异常信息：{}",lastDate,page,pageSize,e.getMessage());
//        }

//    }
}
