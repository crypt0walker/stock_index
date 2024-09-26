package com.async.stock.mapper;

//import com.async.stock.pojo.domain.StockUpdownDomain;
import com.async.stock.pojo.domain.StockUpdownDomain;
import com.async.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

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

    List<StockUpdownDomain> getStockUpDownInfos(Date lastDate);
//
//    List<Map> getStockUpDownCount(Date startTime, Date endTime, int i);

    /**
     * 根据指定的时间点查询股票数据
     * @param timePoint
     * @return
     */
//    List<StockUpdownDomain> getStockUpDownInfos(@Param("timePoint") Date timePoint);

    /**
     * 查询指定日期范围内股票的涨停或者跌停的统计数量
     * @param startTime 开始时间，一般是开盘时间
     * @param endTime 截止时间
     * @param flag 涨跌停表示，1：涨停 0：跌停
     * @return
     */
//    List<Map> getStockUpDownCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("flag") int flag);

}
