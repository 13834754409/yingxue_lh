package com.lih.controller;

import com.lih.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/24 19:11
 */
@Controller
@RequestMapping("log")
public class LogController {
    @Autowired
    private LogService logService;

    @RequestMapping("findAll")
    @ResponseBody
    public Map<String,Object> findAll(Integer page,Integer rows){
        return logService.findAll(page,rows);
    }
}

