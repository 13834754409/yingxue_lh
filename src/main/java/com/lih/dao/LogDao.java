package com.lih.dao;

import com.lih.entity.Log;
import com.lih.entity.LogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface LogDao extends Mapper<Log> {
}