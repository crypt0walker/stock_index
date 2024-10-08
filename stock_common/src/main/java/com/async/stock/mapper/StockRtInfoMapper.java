package com.async.stock.mapper;

//import com.async.stock.pojo.domain.StockUpdownDomain;
import com.async.stock.pojo.domain.Stock4EvrDayDomain;
import com.async.stock.pojo.domain.Stock4MinuteDomain;
import com.async.stock.pojo.domain.StockUpdownDomain;
import com.async.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 16232
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-09-22 22:21:20
* @Entity com.async.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);

//    List<StockUpdownDomain> getStockUpDownInfos(Date lastDate);

    List<StockUpdownDomain> getNewestStockUpDownInfos(Date lastDate);

    /**
     * 检查某个 stock_code 和 cur_time 是否已经存在
     * @param stockCode 股票代码
     * @param curTime 当前时间
     * @return 如果存在返回1，否则返回0
     */
    int checkIfExists(@Param("stockCode") String stockCode, @Param("curTime") Date curTime);

    /**
     * 插入股票实时数据
     *
     * @param info 股票实时信息对象
     * @return
     */
    int insertData(StockRtInfo info);

    /**
     * 更新股票实时数据
     *
     * @param info 股票实时信息对象
     * @return
     */
    int updateData(StockRtInfo info);

    /**
     * 根据指定的时间点查询股票数据
     * @param timePoint
     * @return
     */
    List<StockUpdownDomain> getStockUpDownInfos(@Param("timePoint") Date timePoint);
    /**
     * 查询指定时间范围内每分钟涨停或者跌停的数量
     * @param startTime 开始时间
     * @param endTime 结束时间 一般开始时间和结束时间在同一天
     * @param flag 约定:1->涨停 0:->跌停
     * @return
     */
    @MapKey("time")
    List<Map> getStockUpDownCount(@Param("openTime") Date startTime, @Param("curTime") Date endTime, @Param("flag") int flag);

    /**
     * 统计指定时间点下，各个涨跌区间内股票的个数
     * @param avlDate
     * @return
     */
    @MapKey("UpDownSection")
    List<Map> getStockUpDownSectionByTime(@Param("avlDate") Date avlDate);

    /**
     * 根据时间范围查询指定股票的交易流水
     * @param stockCode 股票code
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @return
     */
    List<Stock4MinuteDomain> getStockInfoByCodeAndDate(@Param("stockCode") String stockCode,
                                                       @Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    /**
     * 查询指定日期范围内指定股票每天的交易数据
     * @param stockCode 股票code
     * @param startTime 起始时间
     * @param endTime 终止时间
     * @return
     */
    List<Stock4EvrDayDomain> getStockInfo4EvrDay(@Param("stockCode") String stockCode,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);

    /**
     * 批量插入功能
     * @param stockRtInfoList
     */
    int insertBatch(List<StockRtInfo> stockRtInfoList);

    List<String> getAllStockIds();

    /**
     * 批量插入个股数据
     * @param list
     * @return
     */
    int insertAllData(@Param("list") List<StockRtInfo> list);
}
