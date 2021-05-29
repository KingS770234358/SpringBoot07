package com.wq.controller;

import com.wq.pojo.User;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试Swagger使用的控制器
 */
@Controller
public class HelloController {
    @ResponseBody
    @RequestMapping({"","/hello"})
    public String hello(){
        return "hello";
    }

    @RequestMapping("/SwaggerScanningUser")
    @ResponseBody
    public User SwaggerScanningUSer(){
        return new User();
    }
    // @ApiParam
    @RequestMapping("/ApiParam")
    @ResponseBody
    public String ApiParam(@ApiParam("传入的参数的注释") String ApiParam){
        return ApiParam;
    }

    // Swagger测试
    @GetMapping("/swaggerTest")
    @ResponseBody
    public String swaggerTest(User user){
        System.out.println(user);
        return "test";
    }
}
