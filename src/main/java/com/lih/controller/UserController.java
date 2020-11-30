package com.lih.controller;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lih.entity.User;
import com.lih.po.CityPO;
import com.lih.po.SexPO;
import com.lih.service.UserService;
import com.lih.util.AliyunUtils;
import com.lih.util.MonthUtil;
import io.goeasy.GoEasy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/19 14:57
 */
@Controller
@RequestMapping("user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    @ResponseBody
    @RequestMapping("findAll")
    public Map<String, Object> findAll(Integer page, Integer rows) {
        HashMap<String, Object> findAllMap = new HashMap<>();
        List<User> userList = userService.findAll(page, rows);
        log.debug("查到的用户有: {}", userList);
        Integer count = userService.count();
        log.debug("查到的数量: {}", count);
        Integer total = count % rows == 0 ? count / rows : count / rows + 1;
        findAllMap.put("page", page);
        findAllMap.put("total", total);
        findAllMap.put("records", count);
        findAllMap.put("rows", userList);
        return findAllMap;
    }

    @RequestMapping("status")
    @ResponseBody
    public Map<String, String> status(User user) {
        log.debug("用户为:{}", user);
        HashMap<String, String> map = new HashMap<>();
        try {
            userService.updateStatus(user);
            map.put("message", "修改成功");
            map.put("status", "200");
        } catch (Exception e) {
            map.put("message", "修改失败");
            map.put("status", "404");
        }
        return map;
    }

    @RequestMapping("add")
    @ResponseBody
    public Map<String, Object> add(User user, String oper) {
        HashMap<String, Object> map = new HashMap<>();
        if ("add".equals(oper)) userService.addUser(user);
        map.put("rows", user);


        HashMap<String, Object> maps = new HashMap<>();

        List<Integer> list = MonthUtil.queryMonths();
        List<Integer> boys = new ArrayList<>();
        List<Integer> gitls = new ArrayList<>();
        List<String> monthName = new ArrayList<>();


        String mm = new SimpleDateFormat("MM").format(new Date());
        Integer month = Integer.valueOf(mm);
        for (Integer integer : list) {
            boys.add(userService.findAllByMonth("男", integer));
            gitls.add(userService.findAllByMonth("女", integer));
            monthName.add(integer + "月");
        }
        maps.put("months", monthName);
        maps.put("boys", boys);
        maps.put("gitls", gitls);

        String mapMo = JSON.toJSONString(maps);


        //创建GoEasy对象  参数:机房地区,appkey
        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io", "BC-df0a45499f274b2bae29ae50a6a12dc9");
        //发送消息   参数:通道名称,消息内容
        goEasy.publish("yingx-user", mapMo);

        return map;
    }

    @RequestMapping("showUpload")
    public void showUpload(MultipartFile picImg, String id) throws Exception {
        //获取的文件名字
        String oldFilename = picImg.getOriginalFilename();
        String newName = new Date().getTime() + "-" + oldFilename;
        //存储空间名
        String bucketName = "yingx-lih";
        //保存的文件名
        String objectName = "user-pic/" + newName;
        //创建OSS实例
        OSS oss = AliyunUtils.alyunOSS();
        //创建byt数组
        byte[] bytes = null;
        try {
            bytes = picImg.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //上传Byte数组
        oss.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        //关流

        //数据修改
        User user = new User();
        user.setId(id);
        user.setPicImg("https://yingx-lih.oss-cn-beijing.aliyuncs.com/" + objectName);
        String opiName = user.getPicImg().split("user-pic/")[1];
        log.debug("获取的下载名字: {}", opiName);
        oss.getObject(new GetObjectRequest(bucketName, objectName), new File("src/main/webapp/bootstrap/cover/" + opiName));
        oss.shutdown();
        userService.modfiyUser(user);
    }

    @ResponseBody
    @RequestMapping("aliyun")
    public Map<String, Object> aliyun(String phone) {
        HashMap<String, Object> map = new HashMap<>();
        SendSmsResponse aliyun = null;
        try {
            aliyun = userService.aliyun(phone);
            map.put("status", aliyun);
            map.put("message", "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", aliyun);
            map.put("message", "发送失败");
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("easyPOI")
    public Map<String, Object> easyPOI() {
        Map<String, Object> map = null;
        try {
            map = userService.easyPOI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    @ResponseBody
    @RequestMapping("findAllBySes")
    public List<SexPO> findAllBySes() {
        List<SexPO> sexList = new ArrayList<>();
        List<CityPO> cityBoy = userService.findAllBySex("男");
        sexList.add(new SexPO("小男孩", cityBoy));
        List<CityPO> cityGitl = userService.findAllBySex("女");
        sexList.add(new SexPO("小姑娘", cityGitl));
        return sexList;
    }

    @RequestMapping("findAllByMonth")
    @ResponseBody
    public Map<String, Object> findAllByMonth() {
        HashMap<String, Object> map = new HashMap<>();

        List<Integer> list = MonthUtil.queryMonths();
        List<Integer> boys = new ArrayList<>();
        List<Integer> gitls = new ArrayList<>();
        List<String> monthName = new ArrayList<>();


        String mm = new SimpleDateFormat("MM").format(new Date());
        Integer month = Integer.valueOf(mm);
        for (Integer integer : list) {
            boys.add(userService.findAllByMonth("男", integer));
            gitls.add(userService.findAllByMonth("女", integer));
            monthName.add(integer + "月");
        }
        map.put("months", monthName);
        map.put("boys", boys);
        map.put("gitls", gitls);
        return map;

    }


}
