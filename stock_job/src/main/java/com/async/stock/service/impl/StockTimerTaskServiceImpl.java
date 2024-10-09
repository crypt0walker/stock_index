package com.async.stock.service.impl;
import com.async.stock.face.impl.StockCacheFaceImpl;
import com.async.stock.mapper.*;
import com.async.stock.pojo.entity.StockBlockRtInfo;
import com.async.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.async.stock.utils.ParseType;
import com.async.stock.pojo.vo.StockInfoConfig;
import com.async.stock.pojo.entity.StockMarketIndexInfo;
import com.async.stock.pojo.entity.StockRtInfo;
import com.async.stock.service.StockTimerTaskService;
import com.async.stock.utils.DateTimeUtil;
import com.async.stock.utils.IdWorker;
import com.async.stock.utils.ParserStockInfoUtil;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service()
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockInfoConfig stockInfoConfig;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    /**
     * 必须保证该对象无状态，就是别的对象不能调用方法修改他里面的数据，当然这个对象修改不了数据
     */
    private HttpEntity<Object> httpEntity;
    @Override
    public void getInnerMarketInfo() {
        //1.定义采集的url接口
        String url=stockInfoConfig.getMarketUrl() + String.join(",",stockInfoConfig.getInner());
        //2.调用restTemplate采集数据
        //2.1 组装请求头
        HttpHeaders headers = new HttpHeaders();
        //必须填写，否则数据采集不到
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        headers.add("Accept","*/*");
//        headers.add("Accept-Encoding", "gzip, deflate");
        headers.add("connection", "keep-alive");
        //2.2 组装请求对象
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        //2.3 resetTemplate发起请求
        String resString = restTemplate.postForObject(url, entity, String.class);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        //判断是否访问成功
//        if(responseEntity.getStatusCodeValue()!=200){
//            //访问失败，抛出异常,显示当前请求失败
//            log.error("当前时间点:{},采集数据失败,http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),responseEntity.getStatusCodeValue());
//            //优化这里可以添加功能发生异常给运维或者工作人员发邮件微信等，
//            //当没请求异常了也没有数据解析所以直接退出
//            return;
//        }
        //log.info("当前采集的数据：{}",resString);
        //3.数据解析（重要）
//        var hq_str_sh000001="上证指数,3267.8103,3283.4261,3236.6951,3290.2561,3236.4791,0,0,402626660,398081845473,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2022-04-07,15:01:09,00,";
//        var hq_str_sz399001="深证成指,12101.371,12172.911,11972.023,12205.097,11971.334,0.000,0.000,47857870369,524892592190.995,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,0,0.000,2022-04-07,15:00:03,00";
//        String jsData = responseEntity.getBody();
        String reg="var hq_str_(.+)=\"(.+)\";";
        //编译表达式,获取编译对象
        Pattern pattern = Pattern.compile(reg);
        //匹配字符串
        Matcher matcher = pattern.matcher(resString);
        ArrayList<StockMarketIndexInfo> list = new ArrayList<>();
        //判断是否有匹配的数值
        while (matcher.find()){
            //获取大盘的code
            String marketCode = matcher.group(1);
            //获取其它信息，字符串以逗号间隔
            String otherInfo=matcher.group(2);
            //以逗号切割字符串，形成数组
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName=splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint=new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint=new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint=new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint=new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint=new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt=Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol=new BigDecimal(splitArr[9]);
            //时间
//            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            // 组合日期和时间字符串
            String dateTimeStr = splitArr[30] + " " + splitArr[31];

// 解析为 DateTime 对象
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime dateTime = DateTime.parse(dateTimeStr, formatter);

// 去除秒部分并转换为 Date
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(dateTime).toDate();

            //组装entity对象
            StockMarketIndexInfo info = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();
            //收集封装的对象，方便批量插入
            list.add(info);
        }
        //定义日志消息答应当前采集的数据
        log.info("采集的当前大盘数据：{}",list);
        //调用mapper接口将数据保存到数据库中
        int row = stockMarketIndexInfoMapper.insertSelectData(list);//因为底层id和curTime建立了唯一索引，所以不能挖掘用一时间数据会报错
        //int row=2;
        if (row>0) {
            //大盘数据采集完毕通知backend工程刷新缓存
            //发送日期对象，接收方通过接收的日期与当前时间比对能判断出数据延迟的时长，用于运维通知
            rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());

            log.info("当前时间点:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            return;
        }else {
            log.info("当前时间点:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            return;
        }
//        log.info("采集的当前大盘数据：{}",list);
//        //批量插入
//        if (CollectionUtils.isEmpty(list)) {
//            return;
//        }
//        int count = this.stockMarketIndexInfoMapper.insertBatch(list);
//        log.info("批量插入了：{}条数据",count);
    }

    //注入格式解析bean
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;

    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;



    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 注入线程池对象
     */
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 批量获取股票分时数据详情信息
     * http://hq.sinajs.cn/list=sz000002,sh600015
     */
//    @Override
//    public void getStockRtIndex() {
//        //批量获取股票ID集合
////        List<String> stockIds = stockRtInfoMapper.getAllStockIds();
//        List<String> stockIds = stockBusinessMapper.getAllStockCode();
//        //计算出符合sina命名规范的股票id数据
//        stockIds = stockIds.stream().map(id -> {
//            return id.startsWith("6") ? "sh" + id : "sz" + id;
//        }).collect(Collectors.toList());
//        //设置公共请求头对象
//        //设置请求头数据
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer","https://finance.sina.com.cn/");
//        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        //一次性查询过多，我们将需要查询的数据先进行分片处理，每次最多查询20条股票数据
//        //此处改进，使用线程池与mq
//        //每个分片的数据开启一个线程异步执行任务
//        Lists.partition(stockIds, 20).forEach(list -> {
//            threadPoolTaskExecutor.execute(()-> {
//                //拼接股票url地址
//                String stockUrl = stockInfoConfig.getMarketUrl() + String.join(",", list);
//                //获取响应数据
//                String result = restTemplate.postForObject(stockUrl, entity, String.class);
//                List infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
//                log.info("数据量：{}", infos.size());
//                //            log.info("数据：{}",infos);
//                int count = stockRtInfoMapper.insertBatch(infos);
//                log.info("插入数据量：{}", count);
//                //通知后台终端刷新本地缓存，发送的日期数据是告知对方当前更新的股票数据所在时间点
//                rabbitTemplate.convertAndSend("stockExchange", "inner.market", new Date());
//            });
//        });
//    }
    /**
     * 批量获取股票分时数据详情信息
     * http://hq.sinajs.cn/list=sz000002,sh600015
     * referer：https://finance.sina.com.cn/
     */
//    @Override
//    public void getStockRtIndex() {
//        //批量获取股票ID集合
//        List<String> stockIds = stockBusinessMapper.getStockIds();
//        //计算出符合sina命名规范的股票id数据
//        stockIds = stockIds.stream().map(id -> {
//            return id.startsWith("6") ? "sh" + id : "sz" + id;
//        }).collect(Collectors.toList());
//        //一次性查询过多，我们将需要查询的数据先进行分片处理，每次最多查询20条股票数据
//        Lists.partition(stockIds,20).forEach(list->{
//            //拼接股票url地址
//            String stockUrl=stockInfoConfig.getMarketUrl()+String.join(",",list);
//            //获取响应数据
//            String result = restTemplate.getForObject(stockUrl, String.class);
//            List<StockRtInfo> infos = parserStockInfoUtil.parser4StockOrMarketInfo(result, ParseType.ASHARE);
//            log.info("数据量：{}",infos.size());
//            //批量插入数据库
//            stockRtInfoMapper.insertBatch(infos);
//        });
//    }
    @Autowired
    private StockCacheFaceImpl stockCacheFace;

    /**
     * 定义获取分钟级个股票数据
     */
    @Override
    public void getStockRtIndex() {
        // 获取所有股票代码集合（3000+个），由于数据量太大，需要拆分处理，避免URL过长
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();

        // 将每个股票代码添加大盘业务前缀：sh（上证）或 sz（深证）
        allStockCode = allStockCode.stream()
                .map(code -> code.startsWith("6") ? "sh" + code : "sz" + code)
                .collect(Collectors.toList());

        // 使用缓存获取股票代码（如果需要缓存，可以解除下面这行的注释）
//         List<String> allStockCode = stockCacheFace.getAllStockCodeWithPrefix();

        long startTime = System.currentTimeMillis();

        // 将所有股票代码分成若干个小集合，每组包含4个代码
        Lists.partition(allStockCode, 10).forEach(codes -> {
            // 使用线程池执行每个子集合的请求
            threadPoolTaskExecutor.execute(() -> {
                // 1. 定义采集的url接口，将股票代码拼接到URL中
                String url = stockInfoConfig.getMarketUrl() + String.join(",", codes);

                // 2. 构建HTTP请求头
                HttpHeaders headers = new HttpHeaders();
                headers.add("Referer", "https://finance.sina.com.cn/stock/");
                headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
                headers.add("Accept", "*/*");
                headers.add("connection", "keep-alive");

                // 2.1 构建HTTP实体对象
                HttpEntity<Object> entity = new HttpEntity<>(headers);

                try {
                    // 发送HTTP请求，获取返回的股票数据
                    String resString = restTemplate.postForObject(url, entity, String.class);
                    log.info("当前采集的数据：{}", resString);

                    // 解析获取各个股票数据，并处理日期格式
                    List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(resString, ParseType.ASHARE);
                    log.info("数据量：{}", list.size());
                    log.info("当前时间点:{}, 采集的个股数据:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);

                    // 遍历解析后的股票数据，逐条检查数据库是否已有记录
                    for (StockRtInfo info : list) {
                        try {
                            // 检查数据库中是否存在相同的 stockCode 和 curTime
                            int exists = stockRtInfoMapper.checkIfExists(info.getStockCode(), info.getCurTime());

                            if (exists > 0) {
                                // 如果记录已存在，执行更新操作
                                int updatedRows = stockRtInfoMapper.updateData(info);
                                if (updatedRows > 0) {
                                    log.info("当前时间点:{}, 更新数据:{}成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), info);
                                } else {
                                    log.warn("当前时间点:{}, 更新数据:{}失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), info);
                                }
                            } else {
                                // 如果记录不存在，执行插入操作
                                int insertedRows = stockRtInfoMapper.insertData(info);
                                if (insertedRows > 0) {
                                    log.info("当前时间点:{}, 插入数据:{}成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), info);
                                } else {
                                    log.warn("当前时间点:{}, 插入数据:{}失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), info);
                                }
                            }
                        } catch (Exception e) {
                            log.error("处理数据:{}时出现错误：{}", info, e.getMessage());
                        }
                    }

                    // 记录完成时间并输出耗时
                    Long takeTime = System.currentTimeMillis() - startTime;
                    log.info("本次采集话费时间:{}ms", takeTime);

                } catch (Exception e) {
                    log.error("数据采集过程中出现错误：{}", e.getMessage());
                }
            });
        });
    }

    /**
     * 获取板块数据插入数据库
     */
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Override
    public void getStockSectorRtIndex() {
        //获取板块数据请求的url地址
        String blockUrl = stockInfoConfig.getBlockUrl();
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(blockUrl, HttpMethod.GET, httpEntity, String.class);
        if (responseEntity.getStatusCodeValue()!=200) {
            log.error("当前时间点:{},请求板块数据失败,状态码:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),responseEntity.getStatusCodeValue());
            return;
        }
        //获取响应体js数据
        String body = responseEntity.getBody();
        //调用工具类解析获取各个数据
        List<StockBlockRtInfo> list = parserStockInfoUtil.parse4StockBlock(body);
        log.info("板块数据量：{}",list.size());
        log.info("当前时间点:{},采集的板块数据:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        //数据分片保存到数据库下 行业板块类目大概50个，可每小时查询一次即可
        Lists.partition(list,20).forEach(lists->{
            threadPoolTaskExecutor.execute(()->{
                //将调用mapper接口数据保存到数据库中  //批量插入
                int row = stockBlockRtInfoMapper.insertAllData(lists);
                if (row>0) {
                    log.info("当前时间点:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),lists);
                }else {
                    log.info("当前时间点:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),lists);
                }
            });
        });



    }

    /**
     * 获取国外大盘最新数据
     */
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Override
    public void getOuterMarketInfo() {
        //获取国外大盘名称集合
        List<String> outerMarket = stockInfoConfig.getOuter();
        //获取请求url地址
        String url = stockInfoConfig.getMarketUrl();
        //将url拼接起来
        String marketUrl = url+String.join(",",outerMarket);
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(marketUrl, HttpMethod.GET, httpEntity, String.class);
        //获取响应的js数据
        String body = responseEntity.getBody();
        //调用工具类解析获取各个数据
        List<StockOuterMarketIndexInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(body, ParseType.OUTER);
        log.info("获取外盘数据:{}",list);
        //将调用mapper接口数据保存到数据库中  //批量插入
        int row = stockOuterMarketIndexInfoMapper.insertAllData(list);
        if (row>0) {
            log.info("当前时间点:{},插入国外大盘数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }else {
            log.info("当前时间点:{},插入国外大盘数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }

    }


    /**
     * bean生命周期的初始化方法回掉
     */
    @PostConstruct
    public void initData() {
        //添加请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        //添加用户标识
        httpHeaders.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.3.1 Safari/605.1.15");
        //添加防盗链
        httpHeaders.set("Referer","https://finance.sina.com.cn/");
        //初始化httpEntity
        httpEntity = new HttpEntity<>(httpHeaders);
    }



}