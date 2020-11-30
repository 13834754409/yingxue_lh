package com.lih;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@tk.mybatis.spring.annotation.MapperScan("com.lih.dao")
@MapperScan("com.lih.dao")
@SpringBootApplication
public class YingxueLihApplication {

    public static void main(String[] args) {
        SpringApplication.run(YingxueLihApplication.class, args);
    }

}
