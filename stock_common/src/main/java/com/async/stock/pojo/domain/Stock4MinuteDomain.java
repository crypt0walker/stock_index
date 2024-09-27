package com.async.stock.pojo.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 个股分时数据封装
 */
@ApiModel(value = "Stock4MinuteDomain", description = "个股分时数据封装类")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4MinuteDomain {

    @ApiModelProperty(value = "日期，格式为 yyyy-MM-dd HH:mm", example = "2022-01-28 08:09")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Shanghai")
    private Date date;

    @ApiModelProperty(value = "交易量")
    private Long tradeAmt;

    @ApiModelProperty(value = "股票编码", example = "600519")
    private String code;

    @ApiModelProperty(value = "该分钟内的最低价")
    private BigDecimal lowPrice;

    @ApiModelProperty(value = "前一个交易日的收盘价")
    private BigDecimal preClosePrice;

    @ApiModelProperty(value = "股票名称", example = "贵州茅台")
    private String name;

    @ApiModelProperty(value = "该分钟内的最高价")
    private BigDecimal highPrice;

    @ApiModelProperty(value = "该分钟开始时的开盘价")
    private BigDecimal openPrice;

    @ApiModelProperty(value = "当前交易会话中的总交易金额")
    private BigDecimal tradeVol;

    @ApiModelProperty(value = "当前交易价格")
    private BigDecimal tradePrice;
}
