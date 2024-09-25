package com.async.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author by async
 * @Date 2024/9/25
 * @Description 定义封装多内大盘数据的实体类
 */
@Data
@ApiModel(description = "大盘信息类")
public class InnerMarketDomain {
    /**
     * 大盘编码
     */
    @ApiModelProperty(value = "大盘编码")
    private String code;
    /**
     * 大盘名称
     */
    @ApiModelProperty(value = "大盘名称")
    private String name;
    /**
     * 开盘点
     */
    @ApiModelProperty(value = "开盘点")
    private BigDecimal openPoint;
    /**
     * 当前点
     */
    @ApiModelProperty(value = "当前点")
    private BigDecimal curPoint;
    /**
     * 前收盘点
     */
    @ApiModelProperty(value = "前收盘点")
    private BigDecimal preClosePoint;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量")
    private Long tradeAmt;
    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额")
    private Long tradeVol;
    /**
     * 涨跌值
     */
    @ApiModelProperty(value = "涨跌值")
    private BigDecimal upDown;
    /**
     * 涨幅
     */
    @ApiModelProperty(value = "涨幅")
    private BigDecimal rose;

    /**
     * 振幅
     */
    @ApiModelProperty(value = "振幅")
    private BigDecimal amplitude;
    /**
     * 当前时间
     */
    @ApiModelProperty(value = "当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date curTime;
}