package com.async.stock.mapper;

import com.async.stock.pojo.domain.InnerMarketDomain;
import com.async.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author 16232
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2024-09-22 22:21:20
* @Entity com.async.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

//    List<InnerMarketDomain> getInnerIndexByTimeAndCodes(Date lastDate, List<String> innerCodes);

    /**
     * 根据大盘的id和时间查询大盘信息
     * @param marketIds 大盘id集合
     * @param timePoint 当前时间点（默认精确到分钟）
     * @return
     */
    List<InnerMarketDomain> getMarketInfo(@Param("marketIds") List<String> marketIds, @Param("timePoint") Date timePoint);

    /**
     * 根据时间范围和指定的大盘id统计每分钟的交易量
     * @param markedIds 大盘id集合
     * @param startTime 交易开始时间
     * @param endTime 结束时间
     * @return
     */
    @MapKey("markedIds")
    List<Map> getStockTradeVol(@Param("markedIds") List<String> markedIds,
                               @Param("startTime") Date startTime,
                               @Param("endTime") Date endTime);

    /**
     * 批量插入股票大盘数据
     * @param infos
     */
    int insertBatch(List<StockMarketIndexInfo> infos);

    /**
     * 批量插入最新挖掘的股票大盘数据
     * @param list 封装成list集合了
     */
    int insertSelectData(@Param("list") ArrayList<StockMarketIndexInfo> list);
}
