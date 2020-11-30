package com.lih.service.Impl;

import com.lih.annotcation.AddCache;
import com.lih.dao.AdminDao;
import com.lih.entity.Admin;
import com.lih.service.AdminService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/18 18:24
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;

    /**
     * 管理员登录
     * @param username
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Admin findOne(String username) {
        Admin admin = new Admin();
        admin.setUsername(username);
        Admin admin1 = adminDao.selectOne(admin);
        return admin1;
    }
}
