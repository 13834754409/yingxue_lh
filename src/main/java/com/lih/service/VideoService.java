package com.lih.service;

import com.lih.entity.Video;
import com.lih.po.VideoPO;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author:lih111
 * @Description:
 * @Date:2020/11/23 19:42
 */
public interface VideoService {
    Map<String,Object> findAll(Integer page, Integer rows);
    Map<String,Object> addVideo(Video video);
    Map<String,Object> updateVideo(Video video);
    Map<String,Object> removeVideo(Video video);
    void upload(MultipartFile videoPath, String id);
    void modfiyVideo(MultipartFile videoPath,String id);

    List<VideoPO> queryByReleaseTime();
    List<VideoPO> queryByLikeVideoName(String content);
}
