package com.async.stock.pojo.test;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestEasyExcel {

    /**
     * 初始化测试用户数据。
     * 此方法创建一个列表，然后循环生成具体的用户数据。
     * @return 返回包含十个用户信息的列表。
     */
    public List<TestEasyExcelUser> init(){
        ArrayList<TestEasyExcelUser> users = new ArrayList<>();
        // 循环创建十个用户数据
        for (int i = 0; i < 10; i++) {
            TestEasyExcelUser user = new TestEasyExcelUser();
            user.setAddress("上海" + i);  // 设置用户的地址信息，这里以上海为基础增加编号
            user.setUserName("张三" + i); // 设置用户的名字，编号递增
            user.setBirthday(new Date()); // 设置用户的生日，这里使用当前日期
            user.setAge(10 + i);          // 设置用户的年龄，从10岁开始递增
            users.add(user);              // 将创建的用户对象添加到列表中
        }
        return users;  // 返回填充完毕的用户列表
    }

    /**
     * 测试 EasyExcel 导出功能。
     * 此方法使用 EasyExcel 的 API 来导出一个 Excel 文件。
     */
    @Test
    public void test02(){
        List<TestEasyExcelUser> users = init(); // 调用 init 方法获取用户数据
        // 使用 EasyExcel 的 write 方法来写入数据
        EasyExcel.write("H:\\Java\\project\\stock\\stock_parent\\stock_parent\\test\\TestEasyExcel.xls",
                        TestEasyExcelUser.class)  // 指定文件路径和数据类模型
                .sheet("用户信息")               // 创建并命名新的工作表为“用户信息”
                .doWrite(users);                // 执行写操作，将用户数据列表写入Excel
    }

    /**
     * excel数据格式必须与实体类定义一致，否则数据读取不到
     */
    @Test
    public void readExcel(){
        ArrayList<TestEasyExcelUser> users = new ArrayList<>();
        //读取数据
        EasyExcel.read("H:\\Java\\project\\stock\\stock_parent\\stock_parent\\test\\TestEasyExcel.xls", TestEasyExcelUser.class, new AnalysisEventListener<TestEasyExcelUser>() {
//            @Override
            public void invoke(TestEasyExcelUser o, AnalysisContext analysisContext) {
                System.out.println(o);
                users.add(o);
            }
//            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("完成。。。。");
            }
        }).sheet().doRead();
        System.out.println(users);
    }
}
