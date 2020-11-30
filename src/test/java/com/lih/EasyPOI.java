package com.lih;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.lih.dao.UserDao;
import com.lih.entity.User;
import com.lih.util.AliyunUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/26 18:10
 */
@SpringBootTest
public class EasyPOI {
    @Autowired
    private UserDao userDao;

    @Test
    public void test1() {
        //导入设置的参数
        ImportParams importParams = new ImportParams();
        importParams.setTitleRows(1);//标题所占行
        importParams.setHeadRows(1);//表头所占行
        //导入
        List<User> list = ExcelImportUtil.importExcel(new File("C:\\Users\\XuHua\\Desktop\\项目\\easypoi.xls"), User.class, importParams);
        list.forEach(user -> System.out.println(user));
    }

    @Test
    public void test0() {
        List<User> users = userDao.selectAll();
        //导出设置的参数  参数:大标题,工作表名
        ExportParams exportParams = new ExportParams("用户数据", "用户");
        //导出工具   参数:导出的参数,对应的实体类,导出的集合
        OSS oss = AliyunUtils.alyunOSS();
        String bucketName = "yingx-lih";
        String objectName = null;
        for (int i=0;i<users.size();i++) {
            //String objectName = "video/" + video1.getVideoPath().split("video/")[1];
            objectName = "user-pic/" + users.get(i).getPicImg().split("user-pic/")[1];
            System.out.println(objectName);
            users.get(i).setPicImg("src/main/webapp/bootstrap/cover/" + objectName);
            System.out.println(users.get(i).getPicImg());
            oss.getObject(new GetObjectRequest(bucketName, objectName), new File(users.get(i).getPicImg()));
        }
        oss.shutdown();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, users);
        try {
            workbook.write(new FileOutputStream(new File("C:\\Users\\XuHua\\Desktop\\项目\\userxls.xls")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
