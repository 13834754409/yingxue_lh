package com.lih;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author:lih
 * @Description:
 * @Date:2020/12/01 19:29
 */
@SpringBootTest
public class TestShiro {
    public static void testlogin(String username, String password) {
        //初始化安全管理工厂
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //根据安全管理器工厂初始化安全管理器
        SecurityManager instance = factory.createInstance();
        //将安全管理器交给安全工具类
        SecurityUtils.setSecurityManager(instance);
        //根据安全工具类获取主体对象
        Subject subject = SecurityUtils.getSubject();
        //创建令牌 token=身份信息(username)+凭证信息(password)
        AuthenticationToken token = new UsernamePasswordToken(username, password);
        //认证
        try {
            subject.login(token);
            System.out.println("登录成功");
        } catch (UnknownAccountException e) {
            System.out.println("未知的账号异常  用户名不正确");
        } catch (IncorrectCredentialsException e) {
            System.out.println("不正确的凭证异常   密码错误");
        }
        /**
         * UnknownAccountException: 未知的账号异常   用户名不正确
         * IncorrectCredentialsException:不正确的凭证异常   密码错误
         */
        boolean authenticated = subject.isAuthenticated();
        System.out.println("认证状态:" + authenticated);
    }

    public static void main(String[] args) {
        testlogin("admin","admin1");
    }
}
