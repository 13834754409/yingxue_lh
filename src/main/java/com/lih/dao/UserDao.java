package com.lih.dao;

import com.lih.entity.User;

import java.util.List;

import com.lih.po.CityPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserDao extends Mapper<User> {
     void updateStatus(User user);


     List<CityPO> findAllBySex(String sex);

     Integer findAllByMonth(@Param("sex") String sex,
                                      @Param("month") Integer month);
}