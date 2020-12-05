package com.lih.controller;

import com.alibaba.druid.util.StringUtils;
import com.aliyun.oss.OSS;
import com.lih.entity.Video;
import com.lih.entity.VideoExample;
import com.lih.service.VideoService;
import com.lih.util.AliyunUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/23 19:54
 */
@Controller
@RequestMapping("video")
public class VideoController {
    private static final Logger log = LoggerFactory.getLogger(VideoController.class);
    @Autowired
    private VideoService videoService;

    @RequestMapping("findAll")
    @ResponseBody
    public Map<String,Object> findAll(Integer page,Integer rows){
        return videoService.findAll(page,rows);
    }

    @RequestMapping("edit")
    @ResponseBody
    public Map<String,Object> edit(String oper,Video video){
        Map<String, Object> map = null;
        if(StringUtils.equals("edit",oper)){
            map = videoService.updateVideo(video);
        }
        if (StringUtils.equals("add",oper)){
            map= videoService.addVideo(video);
        }
        if (StringUtils.equals("del",oper)){
            map= videoService.removeVideo(video);
        }
        return map;
    }

    @RequestMapping("upload")
    public void upload(MultipartFile videoPath,String id){
        log.debug("获取的文件名字: {}",videoPath.getOriginalFilename());
        videoService.upload(videoPath,id);
    }

    @RequestMapping("modfiyvideo")
    public void modfiyvideo(MultipartFile videoPath,String id){
        log.debug("获取到的id: {}",id);
        log.debug("获取到的名字: {}",videoPath.getOriginalFilename());
        videoService.modfiyVideo(videoPath,id);
    }

    @RequestMapping("searchVideo")
    @ResponseBody
    public List<Video> searchVideo(String content){
        List<Video> videos = videoService.selectByContent(content);
        return videos;
    }

}

