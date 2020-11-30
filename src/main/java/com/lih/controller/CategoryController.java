package com.lih.controller;

import com.alibaba.druid.util.StringUtils;
import com.lih.dao.CategoryDao;
import com.lih.entity.Category;
import com.lih.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.dc.pr.PRError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/20 16:20
 */
@Controller
@RequestMapping("category")
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @RequestMapping("findAllFirst")
    public Map<String, Object> findAllFirst(Integer page, Integer rows) {
        List<Category> firstList = categoryService.findAllFirst(page, rows);
        log.debug("查到的一级类别数据: {}", firstList);
        Integer countFirst = categoryService.countFirst();
        log.debug("查到的一级类别数量: {}", countFirst);
        HashMap<String, Object> map = new HashMap<>();
        Integer total = countFirst % rows == 0 ? countFirst / rows : countFirst / rows + 1;
        map.put("page", page);
        map.put("total", total);
        map.put("records", countFirst);
        map.put("rows", firstList);
        return map;
    }

    @ResponseBody
    @RequestMapping("findAllSecond")
    public Map<String, Object> findAllSecond(Integer page, Integer rows, String id) {
        List<Category> secondList = categoryService.findAllSecond(page, rows, id);
        log.debug("查到的二级类别数据: {}", secondList);
        Integer countSecond = categoryService.countSecond(id);
        log.debug("查到的二级类别数量: {}", countSecond);
        HashMap<String, Object> map = new HashMap<>();
        Integer total = countSecond % rows == 0 ? countSecond / rows : countSecond / rows + 1;
        map.put("page", page);
        map.put("total", total);
        map.put("records", countSecond);
        map.put("rows", secondList);
        return map;
    }

    @RequestMapping("edit")
    @ResponseBody
    public Map<String, Object> edit(Category category, String oper, String editid, HttpServletRequest request) {
        Map<String, Object> map = null;
        if (StringUtils.equals("add", oper)) {
            log.debug("获取的id:{}", editid);
            if (editid != null) {
                category.setLevels(2);
                category.setParentid(editid);
            } else {
                category.setLevels(1);
                category.setParentid(null);
            }
            map = categoryService.add(category);
        }
        if (StringUtils.equals("edit", oper)) {
            map = categoryService.update(category);
        }

        if (StringUtils.equals("del", oper)) {
            map = categoryService.remove(category);
        }

        Set<Map.Entry<String, Object>> entries = map.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("findAllSecondByAll")
    public void findAllSecondByAll(HttpServletResponse response) {
        List<Category> allSecondByAll = categoryService.findAllSecondByAll();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<select>");
        for (Category category : allSecondByAll) {
            stringBuilder.append("<option value=" + category.getId() + ">" + category.getCatename() + "</option>");
        }
        stringBuilder.append("</select>");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        try {
            response.getWriter().println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
