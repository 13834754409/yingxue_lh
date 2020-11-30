package com.lih.service.Impl;

import com.lih.annotcation.AddCache;
import com.lih.annotcation.AddLog;
import com.lih.annotcation.DelCache;
import com.lih.dao.CategoryDao;
import com.lih.dao.VideoDao;
import com.lih.entity.Category;
import com.lih.entity.CategoryExample;
import com.lih.entity.Video;
import com.lih.entity.VideoExample;
import com.lih.po.CategoryPO;
import com.lih.service.CategoryService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/20 16:12
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private VideoDao videoDao;

    /**
     * 分页查询所有一级类别
     *
     * @param page
     * @param rows
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> findAllFirst(Integer page, Integer rows) {
        Integer pageNum = (page - 1) * rows;
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.createCriteria().andLevelsEqualTo(1);
        RowBounds rowBounds = new RowBounds(pageNum, rows);
        return categoryDao.selectByExampleAndRowBounds(categoryExample, rowBounds);
    }

    /**
     * 查询一级类别的总条数
     *
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer countFirst() {
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(1);
        return categoryDao.selectCountByExample(example);
    }

    /**
     * 查询二级类别的总条数
     *
     * @param parentid
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer countSecond(String parentid) {
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(2).andParentidEqualTo(parentid);
        return categoryDao.selectCountByExample(example);
    }

    /**
     * 分页查询所有二级类别
     *
     * @param page
     * @param rows
     * @param parentid
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> findAllSecond(Integer page, Integer rows, String parentid) {
        Integer pageNum = (page - 1) * rows;
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(2).andParentidEqualTo(parentid);
        RowBounds rowBounds = new RowBounds(pageNum, rows);
        return categoryDao.selectByExampleAndRowBounds(example, rowBounds);
    }

    /**
     * 查询所有二级类别
     *
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> findAllSecondByAll() {
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(2);
        return categoryDao.selectByExample(example);
    }

    /**
     * 级别添加
     *
     * @param category
     * @return
     */
    @DelCache
    @Override
    @AddLog("级别添加")
    public Map<String, Object> add(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            category.setId(UUID.randomUUID().toString());
            categoryDao.insertSelective(category);
            map.put("message", "类别添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "类别添加失败");
        }
        return map;

    }

    /**
     * 级别修改
     *
     * @param category
     * @return
     */
    @DelCache
    @AddLog("级别修改")
    @Override
    public Map<String, Object> update(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            categoryDao.updateByPrimaryKeySelective(category);
            map.put("message", "类别修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "类别修改失败");
        }
        return map;
    }


    /**
     * 级别删除
     *
     * @param category
     * @return
     */
    @DelCache
    @Override
    @AddLog("级别删除")
    public Map<String, Object> remove(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        Category categorys = categoryDao.selectOne(category);
        if (categorys.getLevels() == 1) {
            //判断是否有子类别
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentidEqualTo(category.getId());
            //根据以及类别查询对应二级类别的数量
            int count = categoryDao.selectCountByExample(example);
            if (count == 0) {
                //删除一级
                categoryDao.deleteByPrimaryKey(category);
                map.put("message", "删除成功");
            } else {
                throw new RuntimeException("删除一级类别失败,请检查");
            }
        } else {
            //判断该类别下是否有视频
            VideoExample videoExample = new VideoExample();
            videoExample.createCriteria().andCategoryIdEqualTo(categorys.getId());
            int videocount = videoDao.selectCountByExample(videoExample);
            log.debug("查询视频的数量: {}", videocount);
            if (videocount == 0) {
                //没有视频
                categoryDao.deleteByPrimaryKey(category);
                map.put("message", "删除成功");
            } else {
                throw new RuntimeException("删除二级类别失败,请检查");
            }

        }
        return map;
    }

    /**
     * 前台接口
     * @return
     */
    @AddCache
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CategoryPO> queryAllCategory() {
        return categoryDao.queryAllCategory();
    }

}
