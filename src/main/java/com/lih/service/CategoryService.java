package com.lih.service;

import com.lih.entity.Category;
import com.lih.po.CategoryPO;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/20 16:02
 */
public interface CategoryService {
    /**
     * 分页查询所有一级类别
     * @param page
     * @param rows
     * @return
     */
    List<Category> findAllFirst(Integer page,Integer rows);

    /**
     * 查询一级类别的总条数
     * @return
     */
    Integer countFirst();

    /**
     * 查询二级类别的总条数
     * @param parentid
     * @return
     */
    Integer countSecond(String parentid);

    /**
     * 分页查询所有二级类别
     * @param page
     * @param rows
     * @param parentid
     * @return
     */
    List<Category> findAllSecond(Integer page, Integer rows, String parentid);

    Map<String,Object> add(Category category);
    Map<String,Object> update(Category category);
    Map<String,Object> remove(Category category);
    /**
     * 查询所有二级类别
     */
    List<Category> findAllSecondByAll();

    List<CategoryPO> queryAllCategory();
}
