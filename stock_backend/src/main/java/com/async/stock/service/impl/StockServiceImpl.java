package com.async.stock.service.impl;

//import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcel;
import com.async.stock.mapper.StockBlockRtInfoMapper;
import com.async.stock.mapper.StockMarketIndexInfoMapper;
import com.async.stock.mapper.StockRtInfoMapper;
import com.async.stock.pojo.domain.*;
import com.async.stock.service.StockService;
import com.async.stock.utils.DateTimeUtil;
import com.async.stock.vo.resp.PageResult;
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
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;

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
    @Override
    public R<PageResult<StockUpdownDomain>> getPageStockInfos(Integer page, Integer pageSize) {
        //1.获取最新的股票交易时间
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO 伪造数据，后续删除
        lastDate=DateTime.parse("2022-07-07 14:43:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.设置分页参数
        PageHelper.startPage(page,pageSize);
        //3.调用mapper查询数据
        List<StockUpdownDomain> infos=stockRtInfoMapper.getStockUpDownInfos(lastDate);
        //判断数据是否为空
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //3.组装数据
        //转化成PageInfo对象
        //PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(infos);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(new PageInfo<>(infos));
        //4.响应数据
        return R.ok(pageResult);
    }

    @Override
    public R<List<StockUpdownDomain>> getNewestStockInfos() {
        //1.获取最新的股票交易时间
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO 伪造数据，后续删除
        lastDate=DateTime.parse("2022-07-07 14:43:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.调用mapper查询数据
        List<StockUpdownDomain> infos=stockRtInfoMapper.getNewestStockUpDownInfos(lastDate);
        //判断数据是否为空
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //3.响应数据
        return R.ok(infos);
    }

    /**
     * 统计最新交易日下股票在各个时间点涨跌停的数量
     * @return
     */
    @Override
    public R<Map<String, List>> getStockUpDownCount() {
        //1.获取最新的股票交易时间范围：开盘到最新交易时间点
        //统计最新交易时间，就是先获取最新交易时间点，然后再根据这个交易时间点获取开盘时间和收盘时间
        //1.1 获取最新交易时间点，就是截止截止时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endTime = endDateTime.toDate();
        //假数据 todo
        endTime=DateTime.parse("2022-01-06 14:25:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //1.2 获取开始时间
        Date startTime = DateTimeUtil.getOpenDate(endDateTime).toDate();
        //TODO 假数据
        startTime=DateTime.parse("2022-01-06 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.定义flag标识 1：涨停 0：跌停
        //3.分别统计涨停和跌停数据的集合
        //3.1 统计涨停
        List<Map> upList=stockRtInfoMapper.getStockUpDownCount(startTime,endTime,1);
        //3.2 统计跌停
        List<Map> downList=stockRtInfoMapper.getStockUpDownCount(startTime,endTime,0);
        //4.组装数据
        Map<String,List> infos=new HashMap();
        infos.put("upList",upList);
        infos.put("downList",downList);
        //5.响应
        return R.ok(infos);
    }

    /**
     * 导致指定页码的股票涨幅数据到excel
     * @param page 当前页
     * @param pageSize 每页大小
     * @param response
     */
    @Override
    public void exportPageStockInfos(Integer page, Integer pageSize, HttpServletResponse response) {
        //1.获取分页数据
        Date lastDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //TODO 伪造数据，后续删除
        lastDate=DateTime.parse("2022-07-07 14:43:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        PageHelper.startPage(page,pageSize);
        List<StockUpdownDomain> infos=stockRtInfoMapper.getStockUpDownInfos(lastDate);
        response.setCharacterEncoding("utf-8");
        try {
            //2.判断分页数据是否为空，为空则响应json格式的提示信息
            if (CollectionUtils.isEmpty(infos)) {
                R<Object> error = R.error(ResponseCode.NO_RESPONSE_DATA);
                //将error转化成json格式字符串
                String jsonData = new ObjectMapper().writeValueAsString(error);
                //设置响应的数据格式 告知浏览器传入的数据格式
                response.setContentType("application/json");
                //设置编码格式
    //            response.setCharacterEncoding("utf-8");
                //响应数据
                response.getWriter().write(jsonData);
                return;
            }
            //3.调动EasyExcel数据导出
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("股票涨幅数据表格", "UTF-8");
            //指定excel导出时默认的文件名称，说白了就是告诉浏览器下载文件时默认的名称为：股票涨幅数据表格
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨幅信息").doWrite(infos);
        } catch (Exception e) {
            log.error("导出时间：{},当初页码：{}，导出数据量：{}，发生异常信息：{}",lastDate,page,pageSize,e.getMessage());
        }
    }

    @Override
    public R<Map> stockTradeVol4InnerMarket() {
        //1.获取当前时间cur_time、当前日期T。如果当前未开市则获取前一个开市日日期，时间设置为最后。
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最近股票有效交易时间点--T日时间范围
        DateTime lastDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDateTime);
        //转化成java中Date,这样jdbc默认识别
        Date startTime4T = openDateTime.toDate();
        Date endTime4T=lastDateTime.toDate();
        //TODO  mock数据
        startTime4T=DateTime.parse("2022-01-03 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        endTime4T=DateTime.parse("2022-01-03 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //1.2 获取T-1日的区间范围
        //获取lastDateTime的上一个股票有效交易日
        DateTime preLastDateTime = DateTimeUtil.getPreviousTradingDay(lastDateTime);
        DateTime preOpenDateTime = DateTimeUtil.getOpenDate(preLastDateTime);
        //转化成java中Date,这样jdbc默认识别
        Date startTime4PreT = preOpenDateTime.toDate();
        Date endTime4PreT=preLastDateTime.toDate();
        //TODO  mock数据
        startTime4PreT=DateTime.parse("2022-01-02 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        endTime4PreT=DateTime.parse("2022-01-02 14:40:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //2.获取上证和深证的配置的大盘id
        //2.1 获取大盘的id集合
        List<String> markedIds = stockInfoConfig.getInner();
        //3.分别查询T日和T-1日的交易量数据，得到两个集合
        //3.1 查询T日大盘交易统计数据
        List<Map> data4T=stockMarketIndexInfoMapper.getStockTradeVol(markedIds,startTime4T,endTime4T);
        if (CollectionUtils.isEmpty(data4T)) {
            data4T=new ArrayList<>();
        }
        //3.2 查询T-1日大盘交易统计数据
        List<Map> data4PreT=stockMarketIndexInfoMapper.getStockTradeVol(markedIds,startTime4PreT,endTime4PreT);
        if (CollectionUtils.isEmpty(data4PreT)) {
            data4PreT=new ArrayList<>();
        }
        //4.组装响应数据
        HashMap<String, List> info = new HashMap<>();
        info.put("amtList",data4T);
        info.put("yesAmtList",data4PreT);
        //5.返回数据
        return R.ok(info);
    }
    /**
     * 查询当前时间下股票的涨跌幅度区间统计功能
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询点
     * @return
     */
    @Override
    public R<Map> stockUpDownScopeCount() {
        //1.获取股票最新一次交易的时间点
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //mock data
        curDate=DateTime.parse("2022-01-06 09:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.查询股票信息
        List<Map> maps=stockRtInfoMapper.getStockUpDownSectionByTime(curDate);
        //2.1 获取有序的标题集合
        List<String> orderSections = stockInfoConfig.getUpDownRange();
        //思路：利用List集合的属性，然后顺序编译，找出每个标题对应的map，然后维护到一个新的List集合下即可
//        List<Map> orderMaps =new ArrayList<>();
//        for (String title : orderSections) {
//            Map map=null;
//            for (Map m : maps) {
//                if (m.containsValue(title)) {
//                    map=m;
//                    break;
//                }
//            }
//            if (map==null) {
//                map=new HashMap();
//                map.put("count",0);
//                map.put("title",title);
//            }
//            orderMaps.add(map);
//        }
        //方式2：使用lambda表达式指定
        List<Map> orderMaps  =  orderSections.stream().map(title->{
            Map mp=null;
            Optional<Map> op = maps.stream().filter(m -> m.containsValue(title)).findFirst();
            //判断是否存在符合过滤条件的元素
            if (op.isPresent()) {
                mp=op.get();
            }else{
                mp=new HashMap();
                mp.put("count",0);
                mp.put("title",title);
            }
            return mp;
        }).collect(Collectors.toList());
        //3.组装数据
        HashMap<String, Object> mapInfo = new HashMap<>();
        //获取指定日期格式的字符串
        String curDateStr = new DateTime(curDate).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        mapInfo.put("time",curDateStr);
        mapInfo.put("infos",orderMaps);
        //4.返回数据
        return R.ok(mapInfo);
    }

    /**
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     *         如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * @param code 股票编码
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> stockScreenTimeSharing(String code) {
        //1.获取最近最新的交易时间点和对应的开盘日期
        //1.1 获取最近有效时间点
        DateTime lastDate4Stock = DateTimeUtil.getLastDate4Stock(DateTime.now());
        Date endTime = lastDate4Stock.toDate();
        //TODO mockdata
        endTime=DateTime.parse("2021-12-30 14:47:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        //1.2 获取最近有效时间点对应的开盘日期
        DateTime openDateTime = DateTimeUtil.getOpenDate(lastDate4Stock);
        Date startTime = openDateTime.toDate();
        //TODO MOCK DATA
        startTime=DateTime.parse("2021-12-30 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //2.根据股票code和日期范围查询
        List<Stock4MinuteDomain> list=stockRtInfoMapper.getStockInfoByCodeAndDate(code,startTime,endTime);
        //判断非空处理
        if (CollectionUtils.isEmpty(list)) {
            list=new ArrayList<>();
        }
        //3.返回响应数据
        return R.ok(list);
    }

}
