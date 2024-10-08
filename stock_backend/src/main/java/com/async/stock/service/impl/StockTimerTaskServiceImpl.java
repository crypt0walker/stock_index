//package com.async.stock.service.impl;
//
//import com.google.common.collect.Lists;
//import com.async.stock.config.vo.StockInfoConfig;
//import com.async.stock.mapper.StockBlockRtInfoMapper;
//import com.async.stock.mapper.StockBusinessMapper;
//import com.async.stock.mapper.StockMarketIndexInfoMapper;
//import com.async.stock.mapper.StockRtInfoMapper;
//import com.async.stock.pojo.entity.StockBlockRtInfo;
//import com.async.stock.pojo.entity.StockMarketIndexInfo;
//import com.async.stock.pojo.entity.StockRtInfo;
//import com.async.stock.service.StockTimerTaskService;
//import com.async.stock.utils.DateTimeUtil;
//import com.async.stock.utils.IdWorker;
//import com.async.stock.utils.ParseType;
//import com.async.stock.utils.ParserStockInfoUtil;
//import com.sun.xml.internal.bind.v2.TODO;
//import lombok.extern.slf4j.Slf4j;
//import org.joda.time.DateTime;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
///**
// * @author async
// * @version 1.0
// */
//@Service("stockTimerTaskService")
//@Slf4j
//public class StockTimerTaskServiceImpl implements StockTimerTaskService {
//
//    @Resource
//    private StockInfoConfig stockInfoConfig;
//
//    @Resource
//    private RestTemplate restTemplate;
//
//    @Resource
//    private IdWorker idWorker;
//
//    @Resource
//    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
//
//    @Resource
//    private StockBusinessMapper stockBusinessMapper;
//
//    @Resource
//    private ParserStockInfoUtil parserStockInfoUtil;
//
//    @Resource
//    private StockRtInfoMapper stockRtInfoMapper;
//
//    @Resource
//    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
//
//    @Resource
//    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
//
//    @Override
//    public void collectInnerMarketInfo() {
//        //1.定义采集的url接口
//        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
//        //2.调用restTemplate发送请求
//        //2.1组装请求头
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer","https://finance.sina.com.cn/stock/");
//        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
//        //2.2组装请求对象
//        HttpEntity<Object> entity = new HttpEntity<>(headers);
//        //2.3发送请求
//        String resString = restTemplate.postForObject(url, entity, String.class);
//        log.info("采集到的数据:{}",resString);
//        //获取公共采集时间点(精准到分钟)
//        Date curDateTime = DateTimeUtil.getDateTimeWithoutSecond(DateTime.now()).toDate();
//
//        //3.数据解析(重要)
//        String reg = "var hq_str_(.+)=\"(.+)\";";
//        //编译表达式
//        Pattern pattern = Pattern.compile(reg);
//        //匹配数据
//        Matcher matcher = pattern.matcher(resString);
//        //
//        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
//        //判断是否匹配到数据
//        while (matcher.find()){
//            //获取股票代码
//            String marketCode = matcher.group(1);
//            //获取其他数据
//            String otherInfo = matcher.group(2);
//            //分割数据 以,间隔 形成数组 0:股票名称 1:当前点数 2:涨跌 3:涨幅 4: 成交量(单位百手) 5:成交额(单位万元)
//            String[] splitArr = otherInfo.split(",");
//            //获取大盘名称
//            String marketName = splitArr[0];
//            //获取当前大盘点数
//            BigDecimal curPoint = new BigDecimal(splitArr[1]);
//            //获取当前大盘涨跌
//            BigDecimal curPrice = new BigDecimal(splitArr[2]);
//            //获取当前大盘涨幅
//            BigDecimal upDownRate = new BigDecimal(splitArr[3]);
//            //获取当前大盘成交量
//            Long tradeVol = Long.valueOf(splitArr[4]);
//            //获取成交金额
//            Long tradeAmount = Long.valueOf(splitArr[5]);
//            //组装entity对象
//            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
//                    .id(idWorker.nextId() + "")
//                    .markId(marketCode)
//                    .markName(marketName)
//                    .curPoint(curPoint)
//                    .currentPrice(curPrice)
//                    .updownRate(upDownRate)
//                    .tradeVolume(tradeVol)
//                    .curTime(curDateTime)
//                    .tradeAccount(tradeAmount).build();
////            log.info("封装的对象信息:{}" , info.toString());
//            //收集封装的对象,方便批量插入
//            list.add(info);
//            log.info("封装的对象信息:{}" , info);
//        }
//        //批量插入数据
//        stockMarketIndexInfoMapper.insertBatch(list);
//
//
//
//    }
//
//    /**
//     * 采集国内A股 股票详情信息
//     */
//    @Override
//    public void collectAShareInfo() {
//        //1.获取所有的股票code集合 (3700+) TODO 后期加入redis缓存
//        List<String> stockCodeList = stockBusinessMapper.getAllStockCode();
//        //转化集合编码 加上前缀
//        stockCodeList = stockCodeList.stream().map(id ->
//        {if(id.startsWith("6")) {
//            id = "sh" + id;
//        }else {
//            id = "sz" + id;
//        }
//            return id;
//        }).collect(Collectors.toList());
//        //构建请求头对象
//        //组装请求头
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer","https://finance.sina.com.cn/stock/");
//        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
//        //组装请求对象
//        HttpEntity<Object> entity = new HttpEntity<>(headers);
//        //2.将股票集合分片处理 ->均等分 比如每份20个 使用到guava工具类
//        /*
//        参数1 : 要分片的集合
//        参数2 : 每个集合元素个数
//         */
////        Lists.partition(stockCodeList, 20).forEach(list -> {
////            //3.为每一份动态拼接url地址,然后通过restTemplate发送请求
////            //格式:http://hq.sinajs.cn/list=sh601003,sh601001,xxx,xxxxx
////            String url = stockInfoConfig.getMarketUrl() + String.join(",", list);
////            //发起请求
////            String resultData = restTemplate.postForObject(url, entity, String.class);
////            //4.解析数据,封装对象
////            List stockRtInfo = parserStockInfoUtil.parser4StockOrMarketInfo(resultData, ParseType.ASHARE);
////            log.info("当前解析的集合数据: {}",stockRtInfo);
////            stockRtInfoMapper.insertBatch(stockRtInfo);
////        });
//        Lists.partition(stockCodeList, 20).forEach(list -> {
//            //加入线程池后 异步多线程处理数据采集提高了操作效率
//           threadPoolTaskExecutor.execute(()->{
//            //3.为每一份动态拼接url地址,然后通过restTemplate发送请求
//            //格式:http://hq.sinajs.cn/list=sh601003,sh601001,xxx,xxxxx
//            String url = stockInfoConfig.getMarketUrl() + String.join(",", list);
//            //发起请求
//            String resultData = restTemplate.postForObject(url, entity, String.class);
//            //4.解析数据,封装对象
//            List stockRtInfo = parserStockInfoUtil.parser4StockOrMarketInfo(resultData, ParseType.ASHARE);
//            log.info("当前解析的集合数据: {}",stockRtInfo);
//            stockRtInfoMapper.insertBatch(stockRtInfo);
//           });
//        });
//    }
//
//    /**
//     * 采集国内板块信息
//     */
//    @Override
//    public void getStockSectorRtIndex() {
//        //1.使用restTemplate发送请求
//        String result = restTemplate.getForObject(stockInfoConfig.getBlockUrl(), String.class);
//        log.info("获取的数据:{}",result);
//        //2.解析数据
//        //2.1使用正则表达式匹配数据 封装对象
//        List<StockBlockRtInfo> blockRtInfoList = parserStockInfoUtil.parse4StockBlock(result);
//        //3.批量插入数据
//
//            Lists.partition(blockRtInfoList, 20).forEach(list -> {
//                threadPoolTaskExecutor.execute(()->{
//                stockBlockRtInfoMapper.insertBatch(list);
//            });
//        });
//    }
//}
