package com.lih.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "yx_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Excel(name = "ID")
    @Id
    private String id;

    @Excel(name = "名字")
    private String nickname;

    @Excel(name = "性别")
    private String sex;

    @Excel(name = "电话")
    private String phone;

    @Excel(name = "头像",type = 2)
    @Column(name = "pic_img")
    private String picImg;

    @Excel(name = "简介")
    private String brief;

    @Excel(name = "学分")
    private String score;

    @Excel(name = "状态")
    private Integer status;

    @Excel(name = "创建日期",exportFormat = "yyyy-MM-dd",importFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date")
    private Date createDate;

    @Excel(name = "城市")
    private String city;

}