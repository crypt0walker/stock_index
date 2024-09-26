package com.async.stock.pojo.test;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//设置单元格大小
@HeadRowHeight(value = 35) // 表头行高
@ContentRowHeight(value = 25) // 内容行高
@ColumnWidth(value = 50) // 列宽
/**
 * 通过注解自定义表头名称 注解添加排序规则，值越大 越靠近右边
 */
public class TestEasyExcelUser {
    @ExcelProperty(value = {"用户基本信息","用户名"},index = 0)
    private String userName;
    @ExcelProperty(value = {"用户基本信息","年龄"},index = 1)
    private Integer age;
//    忽略指定表头信息
    @ExcelIgnore
    @ExcelProperty(value = {"用户基本信息","地址"} ,index = 2)
    private String address;
    @ExcelProperty(value = {"用户基本信息","生日"},index = 3)
    //注意：日期格式注解由alibaba.excel提供
    @DateTimeFormat("yyyy/MM/dd HH:mm")
    private Date birthday;
}
