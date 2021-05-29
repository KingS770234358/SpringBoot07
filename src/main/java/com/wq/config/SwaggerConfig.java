package com.wq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Swagger的配置类
 */
@Configuration
@EnableSwagger2 //开启Swagger2
public class SwaggerConfig {

    // 4.定义多个API接口分组 都注入到Spring容器中
    @Bean
    public Docket getDocketA(Environment environment) {
        Profiles profiles = Profiles.of("dev","test","...");
        boolean flag = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(flag)
                .groupName("A")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wq.controller"))
                .build(); // 建造者模式
    }
    @Bean
    public Docket getDocketB(Environment environment) {
        return new Docket(DocumentationType.SWAGGER_2).groupName("B");
    }
    // 1.配置Swagger的Docket bean实例 注入到容器中
    @Bean
    public Docket getDocket(Environment environment){

        // 3. 设置哪些环境下需要显示swagger
        // 3.1 获取当前项目的环境(被激活的那个) 当前的环境是否是下面其中之一
        Profiles profiles = Profiles.of("dev","test","...");

        // 3.2 这里监听Profiles 需要一个Profile对象 --- 环境变量实例
        // 判断当前环境中的profiles是不是上面定义的那几个profiles之一 返回boolean值
        boolean flag = environment.acceptsProfiles(profiles);

        // 2.设置 docket
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .enable(flag)
                .groupName("WQ")
                .select()
                // RequestHandlerSelectors 配置要扫描接口的方式
                // any 扫描全部
                // none 都不扫描
                // basePackage扫描指定的包 (直接点进RequestHandlerSelectors查看有哪些方法)
                // withClassAnnotation (扫描带有..注解的类) 需要的参数是一个注解的反射对象
                // 下面的例子:只会扫描用@Controller注解标注的类 生成接口
                // withMethodAnnotation (扫描带有..注解的方法)
                // 下面的例子:只会扫描用@RequestMapping注解标注的方法 生成接口
//                .apis(RequestHandlerSelectors.withClassAnnotation(Controller.class))
//                .apis(RequestHandlerSelectors.withMethodAnnotation(RequestMapping.class))
                .apis(RequestHandlerSelectors.basePackage("com.wq.controller"))
                // paths 过滤路径[在上面过滤的基础上 只扫描url映射中带有 /kuang/ 的接口]
                .paths(PathSelectors.ant("/kuang/**"))
                .build(); // 建造者模式
    }

    // 2.配置swagger 文档信息 API Info===没有set方法,需要使用构造器构造
    private ApiInfo getApiInfo(){
        // 作者信息 点击Swagger页面的Send email to WQ会给这个邮箱发邮件
        Contact contact = new Contact("WQ", "http://localhost:8080/07swagger_sb", "77@77");
        return new ApiInfo(
                "WQ的Swagger API文档 测试",
                "这里是API文档的一些描述信息",
                "右上角小logo v1.0",
                "http://localhost:8080/07swagger_sb",
                 contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                 new ArrayList());
    }
}
