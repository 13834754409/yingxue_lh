package com.lih;

import com.lih.dao.UserDao;
import com.lih.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YingxueLihApplicationTests {

    @Autowired
    private UserDao dao;
    @Test
    void contextLoads() {
        User user = new User();
        user.setId("01fefdcb-72de-49ec-a1ab-bab1bc97eb58");
        user.setStatus(5);
        dao.updateByPrimaryKeySelective(user);
    }

}
