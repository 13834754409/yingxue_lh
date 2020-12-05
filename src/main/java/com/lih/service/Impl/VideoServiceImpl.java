package com.lih.service.Impl;

import com.alibaba.fastjson.JSONObject;
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
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.common.Mapper;

import javax.naming.directory.SearchResult;
import javax.persistence.Table;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private RestHighLevelClient restHighLevelClientl;

    /**
     * 分页查询所有视频
     *
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
     *
     * @param video
     * @return
     */
    @DelCache
    @Override
    @AddLog("添加视频")
    public Map<String, Object> addVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        video.setId(uuid);
        video.setUploadTime(new Date());
        try {
            //进行es索引添加数据
            IndexRequest indexRequest = new IndexRequest("yingx", "yx", video.getId());
            indexRequest.source(JSONObject.toJSONStringWithDateFormat(video, "yyyy-MM-dd"), XContentType.JSON);
            IndexResponse index = restHighLevelClientl.index(indexRequest, RequestOptions.DEFAULT);
            log.debug("添加索引状态: {}", index.status());
            videoDao.insertSelective(video);
            map.put("message", "操作成功");
            map.put("status", "200");
            map.put("rows", video);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "添加失败");
            map.put("status", "201");
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
     *
     * @param video
     * @return
     */
    @DelCache
    @Override
    @AddLog("修改视频数据")
    public Map<String, Object> updateVideo(Video video) {
        HashMap<String, Object> map = new HashMap<>();
        video.setVideoPath(null);
        try {

            UpdateRequest updateRequest = new UpdateRequest("yingx", "yx", video.getId());
            updateRequest.doc(JSONObject.toJSONStringWithDateFormat(video, "yyyy-MM-dd"), XContentType.JSON);
            UpdateResponse update = restHighLevelClientl.update(updateRequest, RequestOptions.DEFAULT);
            log.debug("修改索引状态: {}", update.status());

            videoDao.updateByPrimaryKeySelective(video);
            map.put("message", "修改成功");
            map.put("status", "200");
            map.put("rows", video);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "修改失败");
            map.put("status", "201");
        }
        return map;
    }

    /**
     * 文件上传到阿里云
     *
     * @param videoPath
     * @param id
     */
    @Override
    @DelCache
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
        try {
            UpdateRequest updateRequest = new UpdateRequest("yingx", "yx", id);
            updateRequest.doc(JSONObject.toJSONStringWithDateFormat(video, "yyyy-MM-dd"), XContentType.JSON);
            UpdateResponse update = restHighLevelClientl.update(updateRequest, RequestOptions.DEFAULT);
            log.debug("添加索引状态: {}", update.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoDao.updateByExampleSelective(video, videoExample);
    }

    /**
     * 修改视频数据并删除阿里云数据
     *
     * @param videoPath
     * @param id
     */
    @Override
    @DelCache
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

            try {
                UpdateRequest updateRequest = new UpdateRequest("yingx", "yx", id);
                updateRequest.doc(JSONObject.toJSONStringWithDateFormat(video, "yyyy-MM-dd"), XContentType.JSON);
                UpdateResponse update = restHighLevelClientl.update(updateRequest, RequestOptions.DEFAULT);
                log.debug("添加索引状态: {}", update.status());
            } catch (IOException e) {
                e.printStackTrace();
            }
            videoDao.updateByExampleSelective(video, videoExample);
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Video> selectByContent(String content){
        List<Video> videoList = new ArrayList<>();
        try {
            SearchRequest searchRequest = new SearchRequest();
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //设置高亮
            highlightBuilder.field("title").field("brief").requireFieldMatch(false).preTags("<font color='red'>").postTags("</font>");
            //模糊查询类型
            queryBuilder
                    .should(QueryBuilders.wildcardQuery("title",content))//通配符查询
                    .should(QueryBuilders.wildcardQuery("brief",content))//通配符查询
                    .should(QueryBuilders.fuzzyQuery("title",content))//模糊查询
                    .should(QueryBuilders.fuzzyQuery("brief",content))//模糊查询
                    .should(QueryBuilders.queryStringQuery(content).field("title").field("brief"));
            //添加配置分类、高亮、模糊查类型
            sourceBuilder.query(queryBuilder).highlighter(highlightBuilder);
            //配置索引、类型
            searchRequest.indices("yingx").types("yx").source(sourceBuilder);
            SearchResponse search = restHighLevelClientl.search(searchRequest, RequestOptions.DEFAULT);
            //遍历数据
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> asMap = hit.getSourceAsMap();
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();

                Video video = new Video();
                video.setId(hit.getId());
                video.setTitle(String.valueOf(asMap.get("title")));
                if(highlightFields.containsKey("title")){
                    video.setTitle(String.valueOf(highlightFields.get("title").fragments()[0]));
                }
                video.setBrief(String.valueOf(asMap.get("brief")));
                if(highlightFields.containsKey("brief")){
                    video.setBrief(String.valueOf(highlightFields.get("brief").fragments()[0]));
                }
                video.setCoverPath(String.valueOf(asMap.get("coverPath")));
                String uploadTime = String.valueOf(asMap.get("uploadTime"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date parse = format.parse(uploadTime);
                video.setUploadTime(parse);

                videoList.add(video);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return videoList;
    }

    /**
     * 删除视频文件
     *
     * @param video
     * @return
     */
    @DelCache
    @Override
    @AddLog("删除视频数据")
    public Map<String, Object> removeVideo(Video video) {
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
            //删除索引
            DeleteResponse delete = restHighLevelClientl.delete(new DeleteRequest("yingx", "yx", video.getId()), RequestOptions.DEFAULT);
            log.debug("删除索引状态: {}",delete.status());
            videoDao.delete(video);
            map.put("message", "删除成功");
            map.put("status", "200");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "修改失败");
            map.put("status", "201");
        }
        return map;
    }


    /**
     * 前台接口
     *
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
