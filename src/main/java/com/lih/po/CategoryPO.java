package com.lih.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/25 18:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPO implements Serializable {
    /**
     * {
     *             "id": "1",
     *             "cateName": "软件开发",
     *             "levels": 1,
     *             "parentId": "",
     *             "categoryList": [
     */
    private String id;
    private String cateName;
    private Integer levels;
    private String parentId;
    private List<CategoryPO> categoryList;
}
