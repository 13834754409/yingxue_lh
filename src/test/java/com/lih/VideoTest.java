package com.lih;

import com.lih.dao.VideoDao;
import com.lih.entity.Video;
import com.lih.po.VideoPO;
import com.lih.service.Impl.VideoServiceImpl;
import com.lih.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/25 19:43
 */
@SpringBootTest
public class VideoTest {
    @Autowired
    private VideoDao videoDao;

    @Autowired
    private VideoService videoService;
    @Test
    public void test(){
        List<VideoPO> videoPOS = videoDao.queryByLikeVideoName("");
        videoPOS.forEach(v -> System.out.println(v));
    }

    @Test
    public void test0(){
        List<Video> videos = videoService.selectByContent("一");
        videos.forEach(video -> System.out.println(video));
    }
}
