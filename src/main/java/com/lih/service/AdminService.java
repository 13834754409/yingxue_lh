package com.lih.service;

import com.lih.entity.Admin;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/18 18:24
 */

public interface AdminService {
    Admin findOne(String username);
}
