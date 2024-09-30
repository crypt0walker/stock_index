package com.async.stock;

import com.async.stock.service.StockTimerTaskService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRestTemplate {


    @Autowired
    @Qualifier("stockTimerTaskServiceImpl")
//    @Resource(name = "stockTimerTaskServiceImpl")
    private StockTimerTaskService stockTimerService;

    /**
     * 获取大盘数据
     */
    @Test
    public void test02(){
        if (stockTimerService == null) {
            System.out.println("stockTimerService is null");
        }
        stockTimerService.getInnerMarketInfo();
    }

    @Test
    public void test03(){
        if (stockTimerService == null) {
            System.out.println("stockTimerService is null");
        }
        stockTimerService.getStockRtIndex();
    }

    @Test
    public void testPartion() {
        List<Integer> all=new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            all.add(i);
        }
        //将集合均等分，每份大小最多15个
        Lists.partition(all,15).forEach(ids->{
            System.out.println(ids);
        });
    }
}