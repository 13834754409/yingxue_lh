package com.lih.dao;

import com.lih.entity.Video;
import com.lih.entity.VideoExample;
import com.lih.po.VideoPO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;



public interface VideoDao extends Mapper<Video> {
    List<VideoPO> queryByReleaseTime();

    List<VideoPO> queryByLikeVideoName(String content);
}