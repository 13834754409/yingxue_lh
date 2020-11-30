package com.lih;

import com.lih.dao.UserDao;
import com.lih.po.CityPO;
import com.lih.service.UserService;
import com.lih.util.MonthUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/26 20:48
 */
@SpringBootTest
public class TestUser {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @Test
    public void test() {
        List<CityPO> sexs = userService.findAllBySex("女");
        sexs.forEach(ses -> System.out.println(ses));
    }

    @Test
    public void test1() {
        List<Integer> list = MonthUtil.queryMonths();
        ArrayList<Integer> boys = new ArrayList<>();
        for (Integer integer : list) {
            boys.add(userDao.findAllByMonth("男", integer));
        }
        for (Integer boy : boys) {
            System.out.println(boy);
        }
    }


}

