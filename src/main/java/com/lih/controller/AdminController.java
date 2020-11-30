package com.lih.controller;

import com.lih.entity.Admin;
import com.lih.service.AdminService;
import com.lih.util.CreateValidateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:lih
 * @Description:
 * @Date:2020/11/18 18:23
 */
@Controller
@RequestMapping("admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private AdminService adminService;


    @RequestMapping("login")
    @ResponseBody
    public Map<String, String> login(String username, String password, String core, HttpServletRequest request) {
        Admin admin = adminService.findOne(username);
        String code = (String) request.getSession().getAttribute("code");
        log.debug("查到的对象: {}",admin);
        log.debug("获取的验证码: {}",core);
        HashMap<String, String> map = new HashMap<>();
        try {
            if (admin == null) throw new RuntimeException("用户名错误！");
            if (!admin.getPassword().equals(password)) throw new RuntimeException("密码错误！");
            if (!code.equals(core)) throw new RuntimeException("验证码错误！");
            request.getSession().setAttribute("admin", admin);
            map.put("status", "验证成功");
            return map;
        } catch (Exception e) {
            String message = e.getMessage();
            map.put("status", message);
            return map;
        }
    }

    @RequestMapping("getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        CreateValidateCode code = new CreateValidateCode(100, 30, 4, 10);
        log.debug("生成的验证码: {}",code.getCode());
        HttpSession session = request.getSession();
        session.setAttribute("code", code.getCode());
        try {
            code.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("exit")
    public String exit(HttpSession session) {
        session.removeAttribute("admin");
        session.invalidate();
        return "redirect:/login/login.jsp";
    }
}
