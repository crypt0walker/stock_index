//package com.async.stock.controller;
//
//import com.async.stock.service.LogService;
//import com.async.stock.vo.req.LogPageReqVo;
//import com.async.stock.vo.resp.PageResult;
//import com.async.stock.vo.resp.R;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * @author by itheima
// * @Date 2021/12/23
// * @Description
// */
//@RestController
//@RequestMapping("/api")
//public class LogController {
//
//    @Autowired
//    private LogService logService;
//
//
//    /**
//     * 日志信息综合查询
//     * @param vo
//     * @return
//     */
//    @PostMapping("/logs")
//    public R<PageResult> logPageQuery(@RequestBody LogPageReqVo vo){
//        return logService.logPageQuery(vo);
//    }
//
//    /**
//     * 批量删除日志信息功能
//     * @param logIds
//     * @return
//     */
//    @DeleteMapping("/log")
//    public R<String> deleteBatch(@RequestBody List<Long> logIds){
//        return this.logService.deleteBatch(logIds);
//    }
//
//
//}
