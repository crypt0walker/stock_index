package com.async.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by itheima
 * @Date 2021/12/21
 * @Description 用户信息查询vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPageReqVo {
    /**
     * 当前页
     */
    private Integer pageNum;
    /**
     * 每页大小
     */
    private Integer pageSize;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 开始创建日期
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;
    /**
     * 结束创建日期
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
}
