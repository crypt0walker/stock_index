package com.async.stock.service;

import com.async.stock.vo.req.LogPageReqVo;
import com.async.stock.vo.resp.PageResult;
import com.async.stock.vo.resp.R;

import java.util.List;

/**
 * @author daocaoaren
 * @date 2024/7/23 20:53
 * @description :
 */
public interface LogService {
    /**
     * 日志信息分页综合查询
     * @param vo
     * @return
     */
    R<PageResult> logPageQuery(LogPageReqVo vo);

    /**
     * 删除日志信息
     * @param logIds
     * @return
     */
    R<String> deleteBatch(List<Long> logIds);
}
