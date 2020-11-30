package com.lih.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/25 15:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoPO implements Serializable {
    /**
     * "id": "a11282389568441fa166ebedef03e530",
     *             "videoTitle": "人民的名义",
     *             "cover": "http://q40vnlbog.bkt.clouddn.com/1578650041065_人民的名义.jpg",
     *             "path": "http://q3th1ypw9.bkt.clouddn.com/1578650041065_人民的名义.mp4",
     *             "uploadTime": "2020-01-30",
     *             "description": "人民的名义",
     *             "likeCount": 0,      视频数据
     *             "cateName": "Java",   类别名    用户头像
     *             "userPhoto":"http://q40vnlbog.bkt.clouddn.com/1.jpg"
     *
     *
     *
     */
    private String id;          //id
    private String videoTitle;  //视频标题
    private String cover;       //视频封面
    private String path;        //视频
    private Date uploadTime;    //上传日期
    private String description; //视频简介

    private Integer likeCount;   //点赞数

    private String cateName;    //类别名

    private String categoryId;   //级别id
    private String userId;      //用户id
    private String userName;    //用户名称

    private String userPhoto;   //用户头像
}
