package com.lih.dao;

import com.lih.entity.Category;
import com.lih.entity.CategoryExample;
import java.util.List;

import com.lih.po.CategoryPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface CategoryDao extends Mapper<Category> {

    List<CategoryPO> queryAllCategory();

}