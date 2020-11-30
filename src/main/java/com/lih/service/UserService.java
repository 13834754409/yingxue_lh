package com.lih.service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.lih.entity.User;
import com.lih.po.CityPO;

import java.util.List;
import java.util.Map;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/19 14:47
 */
public interface UserService {
    Integer count();
    List<User> findAll(Integer page, Integer rows);
    void updateStatus(User user);
    void addUser(User user);
    void modfiyUser(User user);
    SendSmsResponse aliyun(String phone);

    Map<String,Object> easyPOI();

    List<CityPO> findAllBySex(String sex);

    Integer findAllByMonth(String sex,Integer month);

}
