package com.async.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 股票板块 domain
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "股票板块信息") // Describes the model for Swagger UI
public class StockBlockDomain {
    /**
     * 公司数量
     */
    @ApiModelProperty(value = "公司数量", example = "100") // Describes the property for Swagger UI
    private Integer companyNum;

    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量", example = "1500")
    private Long tradeAmt;

    /**
     * 板块编码
     */
    @ApiModelProperty(value = "板块编码", example = "XJH123")
    private String code;

    /**
     * 平均价
     */
    @ApiModelProperty(value = "平均价格", example = "25.75")
    private BigDecimal avgPrice;

    /**
     * 板块名称
     */
    @ApiModelProperty(value = "板块名称", example = "科技板块")
    private String name;

    /**
     * 当前日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "当前日期", example = "2022-02-28 15:30")
    private Date curDate;

    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额", example = "100000.50")
    private BigDecimal tradeVol;

    /**
     * 涨跌率
     */
    @ApiModelProperty(value = "涨跌率", example = "0.05")
    private BigDecimal updownRate;
}
