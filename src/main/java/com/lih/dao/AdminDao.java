package com.lih.dao;

import com.lih.entity.Admin;
import com.lih.entity.AdminExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface AdminDao extends Mapper<Admin> {
}