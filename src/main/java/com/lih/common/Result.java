package com.lih.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/25 10:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String status;
    private String message;
    private Object data;

    /**
     * 三参
     * @param status  状态
     * @param message 异常信息
     * @param data     数据
     * @return
     */
    public Result success(String status,String message,Object data){
        Result result = new Result();
        result.setStatus(status);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 状态固定
     * @param message 异常信息
     * @param data    数据
     * @return
     */
    public Result success(String message,Object data){
        Result result = new Result();
        result.setStatus("100");
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     *
     * @param data 数据
     * @return
     */
    public Result success(Object data){
        Result result = new Result();
        result.setStatus("100");
        result.setMessage("请求成功");
        result.setData(data);
        return result;
    }

    /**
     *
     * @param message  异常信息
     * @param data     数据
     * @return
     */
    public Result error(String message,Object data){
        Result result = new Result();
        result.setStatus("104");
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     *
     * @param message  异常信息
     * @return
     */
    public Result error(String message){
        Result result = new Result();
        result.setStatus("104");
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     *
     * @return
     */
    public Result error(){
        Result result = new Result();
        result.setStatus("104");
        result.setMessage("请求失败");
        result.setData(null);
        return result;
    }
}
