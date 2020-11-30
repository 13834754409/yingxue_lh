package com.lih.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "yx_video")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video implements Serializable {
    @Id
    private String id;

    private String title;

    private String brief;
    @Column(name = "cover_path")
    private String coverPath;
    @Column(name = "video_path")
    private String videoPath;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "upload_time")
    private Date uploadTime;
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "group_id")
    private String groupId;

}