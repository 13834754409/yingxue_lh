package com.lih;

import com.lih.dao.CategoryDao;
import com.lih.entity.Category;
import com.lih.entity.CategoryExample;
import com.lih.po.CategoryPO;
import com.lih.service.CategoryService;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/20 15:10
 */
@SpringBootTest
public class TestCategory {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;


    @Test
    public void test9(){
        List<CategoryPO> categoryPOS = categoryService.queryAllCategory();
        for (CategoryPO categoryPO : categoryPOS) {
            System.out.println(categoryPO);
        }
    }

    @Test
    public void test0(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andLevelsEqualTo(1);
        RowBounds rowBounds = new RowBounds(0, 5);
        List<Category> categories = categoryDao.selectByExampleAndRowBounds(categoryExample,rowBounds);
        categories.forEach(category1 -> System.out.println(category1));
    }
    @Test
    public void test1(){
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setCatename("PS");
        category.setLevels(2);
        category.setParentid("1");
        categoryDao.insert(category);
    }
    @Test
    public void test2(){
        Category category = new Category();
        category.setId("7");
        Category category1 = categoryDao.selectOne(category);
        category1.setCatename("PSP");
        categoryDao.updateByPrimaryKeySelective(category1);
    }
    @Test
    public void test4(){
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(2).andParentidEqualTo("1");
        RowBounds rowBounds = new RowBounds(0, 5);
        List<Category> categories = categoryDao.selectByExampleAndRowBounds(example,rowBounds);
        categories.forEach(category -> System.out.println(category));
    }
    @Test
    public void test5(){
        List<Category> allFirst = categoryService.findAllFirst(1, 5);
        allFirst.forEach(category -> System.out.println(category));
    }
    @Test
    public void test6(){
        List<Category> allFirst = categoryService.findAllSecond(1, 5,"4");
        allFirst.forEach(category -> System.out.println(category));
    }
    @Test
    public void test7(){
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(2).andParentidEqualTo("1");
        int i = categoryDao.selectCountByExample(example);
        System.out.println(i);
    }
    @Test
    public void tees8(){
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(2).andParentidEqualTo("1");
        List<Category> categories = categoryDao.selectByExample(example);
        categories.forEach(category -> System.out.println(category));
    }
}
