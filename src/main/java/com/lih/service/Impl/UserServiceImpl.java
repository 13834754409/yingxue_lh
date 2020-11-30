package com.lih.service.Impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lih.annotcation.AddCache;
import com.lih.annotcation.AddLog;
import com.lih.annotcation.DelCache;
import com.lih.dao.UserDao;
import com.lih.entity.User;
import com.lih.entity.UserExample;
import com.lih.po.CityPO;
import com.lih.service.UserService;
import com.lih.util.AliyunUtils;
import com.lih.util.CreateValidateCode;
import io.goeasy.GoEasy;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/19 14:50
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<String, Object> easyPOI() {
        Map<String, Object> map = new HashMap<>();
        List<User> users = userDao.selectAll();
        //导出设置的参数  参数:大标题,工作表名
        ExportParams exportParams = new ExportParams("用户数据", "用户");
        //导出工具   参数:导出的参数,对应的实体类,导出的集合
        for (User user : users) {
            String picName = user.getPicImg().split("user-pic/")[1];
            user.setPicImg("src/main/webapp/bootstrap/cover/" + picName);
            log.debug("新路径: {}", user.getPicImg());
        }
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, users);
        try {
            workbook.write(new FileOutputStream(new File("C:\\Users\\XuHua\\Desktop\\userAll.xls")));
            map.put("message", "导出成功!请在桌面查看");
            map.put("status", "200");
        } catch (IOException e) {
            e.printStackTrace();
            map.put("message", "导出失败!");
            map.put("status", "201");
        }
        return map;
    }

    /**
     * 查询总数
     *
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer count() {
        User user = new User();
        return userDao.selectCount(user);
    }

    /**
     * 分页查询所有
     *
     * @param page
     * @param rows
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> findAll(Integer page, Integer rows) {
        Integer pageNow = (page - 1) * rows;
        UserExample userExample = new UserExample();
        RowBounds rowBounds = new RowBounds(pageNow, rows);
        return userDao.selectByExampleAndRowBounds(userExample, rowBounds);
    }

    /**
     * 修改用户数据
     *
     * @param user
     */
    @DelCache
    @Override
    @AddLog("修改用户")
    public void modfiyUser(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public SendSmsResponse aliyun(String phone) {
        String signName = "夜黎明";
        String templateCode = "SMS_205605685";
        CreateValidateCode code = new CreateValidateCode(100, 30, 4, 10);
        String templateParam = code.getCode();
        SendSmsResponse aliyun = null;
        try {
            aliyun = AliyunUtils.aliyun(phone, signName, templateCode, templateParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aliyun;
    }

    /**
     * 添加用户
     *
     * @param user
     */
    @DelCache
    @Override
    @AddLog("添加用户")
    public void addUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setCreateDate(new Date());
        userDao.insertSelective(user);
    }

    @AddCache
    @Override
    public Integer findAllByMonth(String sex, Integer month) {
        return userDao.findAllByMonth(sex,month);
    }

    /**
     * 修改用户数据
     *
     * @param user
     */
    @DelCache
    @Override
    @AddLog("修改用户状态状态")
    public void updateStatus(User user) {
        if (user.getStatus() == 0) {
            user.setStatus(1);
        } else {
            user.setStatus(0);
        }
        userDao.updateStatus(user);
    }

    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CityPO> findAllBySex(String sex) {
        return userDao.findAllBySex(sex);
    }
}
