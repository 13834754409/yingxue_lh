package com.lih;

import com.lih.dao.UserDao;
import com.lih.entity.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/26 10:42
 */
@SpringBootTest
public class TestPOI {
    @Autowired
    private UserDao userDao;

    @Test
    public void test2() {
        try {
            //创建Excel文档
            Workbook workbook = new HSSFWorkbook(new FileInputStream(new File("C:\\Users\\XuHua\\Desktop\\项目\\testpoi.xls")));
            //获取Sheet
            Sheet sheet = workbook.getSheet("学生信息表");
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                //获取行
                Row row = sheet.getRow(i);
                //获取单元格
                String id = row.getCell(0).getStringCellValue();
                //获取单元格内容
                //String id = cell.getStringCellValue();
                String nickname = row.getCell(1).getStringCellValue();
                String sex = row.getCell(2).getStringCellValue();
                String phone = row.getCell(3).getStringCellValue();
                String picImg = row.getCell(4).getStringCellValue();
                String brief = row.getCell(5).getStringCellValue();
                String score = row.getCell(6).getStringCellValue();
                int status = (int) row.getCell(7).getNumericCellValue();
                Date createDate = row.getCell(8).getDateCellValue();
                String city = row.getCell(9).getStringCellValue();

                User user = new User(id, nickname, sex, phone, picImg, brief,
                        score, status, createDate, city);
                System.out.println(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test1() {
        List<User> users = userDao.selectAll();
        //创建一个Excel文档
        Workbook workbook = new HSSFWorkbook();
        //创建一个工作表   参数:工作表表明  默认:sheet1,sheet2....
        Sheet sheet = workbook.createSheet("学生信息表");
        //设置列宽  参数:列索引,列宽  单位 1/256
        sheet.setColumnWidth(3, 20 * 256);

        //创建合并单元格对象  参数:int firstRow(开始行), int lastRow(结束行), int firstCol(开始单元格), int lastCol(结束单元格)
        CellRangeAddress addresses = new CellRangeAddress(0, 0, 0, 9);

        //合并单元格
        sheet.addMergedRegion(addresses);

        //创建字体对象
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");//字体
        font.setBold(true);//加粗
        font.setColor(IndexedColors.GREEN.getIndex());//颜色
        font.setFontHeightInPoints((short) 15);//字号
        font.setItalic(true);//斜体
        font.setUnderline(FontFormatting.U_SINGLE);//下划线

        //创建单元格样式对象
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);//文字居中
        cellStyle1.setFont(font);//设置字体样式


        //创建标题行
        Row r = sheet.createRow(0);
        //创建标题单元格
        Cell c = r.createCell(0);
        //设置单元格样式
        c.setCellStyle(cellStyle1);
        //设置单元格设置数据
        c.setCellValue("用户信息表");


        //目录行
        String[] titles = {"ID", "名字", "性别", "电话", "头像", "简介", "学分", "状态", "创建日期", "城市"};
        //创建一行   参数:行下标(从0开始)
        Row row = sheet.createRow(1);
        //设置行高  参数:行高  单位 1/20
        row.setHeight((short) (20 * 20));

        for (int i = 0; i < titles.length; i++) {
            //创建单元格
            Cell cell = row.createCell(i);
            //设置数据
            cell.setCellValue(titles[i]);
        }
        //创建日期样式对象
        DataFormat dataFormat = workbook.createDataFormat();
        short format = dataFormat.getFormat("yyyy年MM月dd");//设置日期样式

        //创建单元格样式对象
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(format);
        //处理数据
        for (int i = 0; i < users.size(); i++) {
            //创建一行
            Row rows = sheet.createRow(i + 2);
            //创建单元格  设置数据
            rows.createCell(0).setCellValue(users.get(i).getId());
            rows.createCell(1).setCellValue(users.get(i).getNickname());
            rows.createCell(2).setCellValue(users.get(i).getSex());
            rows.createCell(3).setCellValue(users.get(i).getPhone());
            rows.createCell(4).setCellValue(users.get(i).getPicImg());
            rows.createCell(5).setCellValue(users.get(i).getBrief());
            rows.createCell(6).setCellValue(users.get(i).getScore());
            rows.createCell(7).setCellValue(users.get(i).getStatus());
            rows.createCell(8).setCellValue(users.get(i).getCreateDate());
            rows.createCell(9).setCellValue(users.get(i).getCity());
        }

        try {
            workbook.write(new FileOutputStream(new File("C:\\Users\\XuHua\\Desktop\\项目\\testpoi.xls")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test0() {
        //创建一个Excel文档
        Workbook workbook = new HSSFWorkbook();

        //创建一个工作表,  参数：工作表表面，默认：sheet1，sheet2.。。
        Sheet sheet1 = workbook.createSheet("学生信息表1");
        Sheet sheet2 = workbook.createSheet("学生信息表2");
        //创建一行 参数：行下标  从0开始
        Row row = sheet1.createRow(5);
        //创建一个单元格  参数:单元格下标 从0开始
        Cell cell = row.createCell(2);

        //给单元格设置数据
        cell.setCellValue("这是第六行，第3个单元格");
        try {
            workbook.write(new FileOutputStream(new File("C:\\Users\\XuHua\\Desktop\\项目\\testpoi.xls")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

