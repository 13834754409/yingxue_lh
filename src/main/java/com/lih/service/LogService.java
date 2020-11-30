package com.lih.service;

import java.util.Map;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/24 18:26
 */
public interface LogService {
    Map<String,Object> findAll(Integer page, Integer rows);
}
