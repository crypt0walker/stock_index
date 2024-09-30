package com.async.stock.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author by async
 * @Date 2024/9/29
 * @Description 个股日K数据封装
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "Stock4EvrDayDomain", description = "日K数据封装 for individual stocks")
public class Stock4EvrDayDomain {

    @ApiModelProperty(value = "日期，格式为: yyyy-MM-dd HH:mm", example = "2022-01-28 08:09")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date date;

    @ApiModelProperty(value = "交易量", example = "500000")
    private Long tradeAmt;

    @ApiModelProperty(value = "股票编码", example = "600519")
    private String code;

    @ApiModelProperty(value = "最低价", example = "1800.50")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "股票名称", example = "贵州茅台")
    private String name;

    @ApiModelProperty(value = "最高价", example = "1890.88")
    private BigDecimal highPrice;

    @ApiModelProperty(value = "开盘价", example = "1810.00")
    private BigDecimal openPrice;

    @ApiModelProperty(value = "当前交易总金额", example = "9000000.00")
    private BigDecimal tradeVol;

    @ApiModelProperty(value = "当前收盘价格，如果当天未收盘，则为最新价格", example = "1850.75")
    private BigDecimal closePrice;

    @ApiModelProperty(value = "前收盘价", example = "1800.00")
    private BigDecimal preClosePrice;
}
