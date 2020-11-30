package com.lih.app;

import com.lih.common.Result;
import com.lih.entity.Category;
import com.lih.po.CategoryPO;
import com.lih.po.VideoPO;
import com.lih.service.CategoryService;
import com.lih.service.VideoService;
import com.lih.util.AliyunUtils;
import com.lih.util.CreateValidateCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/25 10:22
 */
@RestController
@RequestMapping("app")
public class AppController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 发送验证码
     * @param phone
     * @return
     */
    @RequestMapping("getPhoneCode")
    public Object getPhoneCode(String phone){
        //随机验证码
        CreateValidateCode validateCode = new CreateValidateCode(100,30,4,40);
        String code = validateCode.getCode();
        String message = null;
        try {
            message = AliyunUtils.alyunNote(phone,code);
            return new Result().success(message,phone);
        }catch (Exception e){
            return new Result().error(message);
        }
    }

    /**
     * 按时间对视频进行排序查询
     * @return
     */
    @RequestMapping("queryByReleaseTime")
    public Result queryByReleaseTime(){
        try {
            List<VideoPO> videoPOS = videoService.queryByReleaseTime();
            return new Result().success(videoPOS);
        }catch (Exception e){
            return new Result().error();
        }
    }

    @RequestMapping("queryAllCategory")
    public Result queryAllCategory(){
        try {
            List<CategoryPO> categoryPOS = categoryService.queryAllCategory();
            return new Result().success(categoryPOS);
        }catch (Exception e){
            return new Result().error();
        }
    }
    @RequestMapping("queryByLikeVideoName")
    public Result queryByLikeVideoName(String content){
        try {
            List<VideoPO> videoPOS = videoService.queryByLikeVideoName(content);
            return new Result().success(videoPOS);
        }catch (Exception e) {
            return new Result().error();

        }
    }
}
