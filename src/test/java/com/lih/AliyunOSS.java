package com.lih;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.PutObjectRequest;
import com.lih.util.AliyunUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/23 17:12
 */
@SpringBootTest
public class AliyunOSS {


    //查询所有文件
    @Test
    public void testAll() {
        OSS oss = AliyunUtils.alyunOSS();
        List<Bucket> buckets = oss.listBuckets();
        buckets.forEach(bucket -> System.out.println(bucket));
        oss.shutdown();
    }

    //删除文件夹
    @Test
    public void testDelete() {
        OSS oss = AliyunUtils.alyunOSS();
        oss.deleteBucket("文件夹名");
        oss.shutdown();
    }

    //创建一个文件
    @Test
    public void testQueryPhone() {
        OSS oss = AliyunUtils.alyunOSS();
        String buckeName="yingx-205";
        oss.createBucket(buckeName);
        oss.shutdown();
    }

    //上传
    @Test
    public void addFile(){
        OSS oss = AliyunUtils.alyunOSS();
        String buckeName="yingx-lih";//文件夹名字 储存空间名
        String objectName="草原.mp4";//保存的文件名
        String localFile="C:\\Users\\XuHua\\Desktop\\项目\\video\\草原.mp4";

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(buckeName, objectName, new File(localFile));

        //上传文件
        oss.putObject(putObjectRequest);

        oss.shutdown();
    }
}
