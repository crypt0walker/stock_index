package com.async.stock.vo.resp;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 */
@Data
@ApiModel(description = "用于封装分页信息的工具类")
public class PageResult<T> implements Serializable {
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long totalRows;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private Integer totalPages;

    /**
     * 当前第几页
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;

    /**
     * 每页记录数
     */
    @ApiModelProperty(value = "每页显示的记录数")
    private Integer pageSize;

    /**
     * 当前页记录数
     */
    @ApiModelProperty(value = "当前页的记录数")
    private Integer size;

    /**
     * 结果集
     */
    @ApiModelProperty(value = "当前页的数据列表")
    private List<T> rows;

    /**
     * 分页数据组装
     * @param pageInfo 用于初始化分页结果的PageInfo对象
     */
    public PageResult(PageInfo<T> pageInfo) {
        totalRows = pageInfo.getTotal();
        totalPages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        size = pageInfo.getSize();
        rows = pageInfo.getList();
    }
}
