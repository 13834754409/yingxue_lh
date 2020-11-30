package com.lih.service.Impl;

import com.aliyun.oss.OSS;
import com.lih.annotcation.AddCache;
import com.lih.annotcation.AddLog;
import com.lih.annotcation.DelCache;
import com.lih.dao.VideoDao;
import com.lih.entity.Video;
import com.lih.entity.VideoExample;
import com.lih.po.VideoPO;
import com.lih.service.VideoService;
import com.lih.util.AliyunUtils;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.common.Mapper;

import javax.persistence.Table;
import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/23 19:43
 */
@Service
@Transactional
public class VideoServiceImpl implements VideoService {
    private static final Logger log = LoggerFactory.getLogger(VideoServiceImpl.class);
    @Autowired
    private VideoDao videoDao;

    /**
     * 分页查询所有视频
     * @param page
     * @param rows
     * @return
     */
    @AddCache
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Map<String, Object> findAll(Integer page, Integer rows) {
        Map<String, Object> map = new HashMap<>();
        //设置条件
        VideoExample videoExample = new VideoExample();
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Video> videos = videoDao.selectByExampleAndRowBounds(videoExample, rowBounds);
        //查询总条数
        int count = videoDao.selectCountByExample(videoExample);
        //设置总页数
        Integer total = count % rows == 0 ? count / rows : count / rows + 1;
        //存数据
        map.put("page", page);
        map.put("records", count);
        map.put("total", total);
        map.put("rows", videos);
        return map;
    }

    /**
     * 添加视频
     * @param video
     * @return
     */
    @DelCache
    @Override
    @AddLog("添加视频")
    public Map<String,Object> addVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        video.setId(uuid);
        video.setUploadTime(new Date());
        try {
            videoDao.insertSelective(video);
            map.put("message","操作成功");
            map.put("status","200");
            map.put("rows", video);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message","添加失败");
            map.put("status","201");
        }
        return map;
    }

    @Override
    @AddCache
    public List<VideoPO> queryByLikeVideoName(String content) {
        return videoDao.queryByLikeVideoName(content);
    }

    /**
     * 修改视频数据
     * @param video
     * @return
     */
    @DelCache
    @Override
    @AddLog("修改视频数据")
    public Map<String,Object> updateVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        String id = video.getId();
        video.setVideoPath(null);
        try {
            videoDao.updateByPrimaryKeySelective(video);
            map.put("message","修改成功");
            map.put("status","200");
            map.put("rows",video);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message","修改失败");
            map.put("status","201");
        }
        return map;
    }

    /**
     * 文件上传到阿里云
     * @param videoPath
     * @param id
     */
    @Override
    public void upload(MultipartFile videoPath, String id) {
        //获取名字
        String oldName = videoPath.getOriginalFilename();
        log.debug("获取的旧名字: {}", oldName);

        String newName = new Date().getTime() + "-" + oldName;
        log.debug("获取的新名字: {}", newName);
        //存储空间名
        String bucketName = "yingx-lih";
        //保存的文件名
        String objectName = "video/" + newName;
        //创建OSS实例
        OSS oss = AliyunUtils.alyunOSS();
        //创建byt数组
        byte[] bytes = null;
        try {
            bytes = videoPath.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //上传Byte数组
        oss.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        oss.shutdown();

        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andIdEqualTo(id);
        Video video = new Video();
        video.setCoverPath("https://yingx-lih.oss-cn-beijing.aliyuncs.com/" + objectName + "?x-oss-process=video/snapshot,t_0,f_jpg,w_0,h_0,m_fast,ar_auto");
        video.setVideoPath("https://yingx-lih.oss-cn-beijing.aliyuncs.com/" + objectName);
        videoDao.updateByExampleSelective(video, videoExample);
    }

    /**
     * 删除视频数据并删除阿里云数据
     * @param videoPath
     * @param id
     */
    @Override
    public void modfiyVideo(MultipartFile videoPath, String id) {
        System.out.println(videoPath.isEmpty());
        System.out.println(videoPath);
        System.out.println(videoPath.getOriginalFilename());
        //查找原有数据
        VideoExample videoExample = new VideoExample();
        videoExample.createCriteria().andIdEqualTo(id);
        Video video = videoDao.selectOneByExample(videoExample);
        log.debug("查出的数据: {}", video);

        if (videoPath.isEmpty() == false && videoPath != null && videoPath.getOriginalFilename() != "") {
            //删除阿里云
            String bucketName = "yingx-lih";


            String objectName = "video/" + video.getVideoPath().split("video/")[1];
            OSS oss = AliyunUtils.alyunOSS();
            oss.deleteObject(bucketName, objectName);


            //重新上传
            //获取名字
            String oldName = videoPath.getOriginalFilename();
            log.debug("获取的旧名字: {}", oldName);
            String newName = new Date().getTime() + "-" + oldName;
            log.debug("获取的新名字: {}", newName);
            //保存的文件名
            String objectNewName = "video/" + newName;
            //创建byt数组
            byte[] bytes = null;
            try {
                bytes = videoPath.getBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //上传Byte数组
            oss.putObject(bucketName, objectNewName, new ByteArrayInputStream(bytes));
            oss.shutdown();
            //保存新数据
            video.setCoverPath("https://yingx-lih.oss-cn-beijing.aliyuncs.com/" + objectNewName + "?x-oss-process=video/snapshot,t_0,f_jpg,w_0,h_0,m_fast,ar_auto");
            video.setVideoPath("https://yingx-lih.oss-cn-beijing.aliyuncs.com/" + objectNewName);
            videoDao.updateByExampleSelective(video, videoExample);
        }

    }


    /**
     * 删除视频文件
     * @param video
     * @return
     */
    @DelCache
    @Override
    @AddLog("删除视频数据")
    public Map<String,Object> removeVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        //存储空间名
        String bucketName = "yingx-lih";
        //查找文件
        Video video1 = videoDao.selectOne(video);
        log.debug("查到的数据: {}", video1);
        String objectName = "video/" + video1.getVideoPath().split("video/")[1];
        log.debug("获取的数据: {}", objectName);
        OSS oss = AliyunUtils.alyunOSS();
        oss.deleteObject(bucketName, objectName);
        oss.shutdown();

        try {
            videoDao.delete(video);
            map.put("message","删除成功");
            map.put("status","200");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message","修改失败");
            map.put("status","201");
        }
        return map;
    }


    /**
     * 前台接口
     * @return
     */
    @AddCache
    @Override
    public List<VideoPO> queryByReleaseTime() {
        List<VideoPO> videoPOS = videoDao.queryByReleaseTime();
        for (VideoPO videoPO : videoPOS) {
            String id = videoPO.getId();
            //根据视频id redis查询点赞数
            videoPO.setLikeCount(8);
        }
        return videoPOS;
    }
}
