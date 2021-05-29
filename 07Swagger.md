07Swagger简介
·背景
后端时代: 前端只需要管理静态页面(html),后端使用模板引擎(JSP)==>后端是主力
前后端分离时代: 常用的框架 Vue + SpringBoot
前后端分离:
  1.后端:后端 控制层(Controller) 服务层(Service) 数据持久层(Dao) [后端团队]
  2.前端:前端控制层(汇集数据) 视图层 [前端团队]
       前端可以使用js制造后端数据(固定写死的)json,不需要后端,前端的工程就可以跑起来
  3.前后端如何交互: API接口[就是Controller的RequestMapping] json数据传输
    前后端相对独立: 松耦合
    前后端甚至可以部署在不同的服务器上
  4.产生问题:
    前后端集成联调:前后端工作人员无法及时协商、尽早解决
  5.解决方案:
    定制schema(计划提纲), 实时更新最新的API, 降低集成风险
    早年:制定word计划文档(git)
    前后端分离:后端更新,前端生成界面实时展示API,并且前端还能测试这个API
              前端测试后端的接口API:POSTMAN
              [后端提供的接口需要实时更新最新的改动消息]  
·Swagger应运而生
 1.概述:
 号称世界上最流行的API框架(实时更新API的文档)
 RestFul风格的API(API文档在线生成工具) [后台的API定义和API文档同步更新]
 可以直接运行, 在线测试API接口;支持多种语言:java php ...
 官网:https://swagger.io/
2.在项目中使用swagger
  导入pom.xml中导入SpringFox jar包
  swagger2 和 swagger-ui(展示API的界面)

3.SpringBoot集成Swagger
3.1 新建项目
3.2 导入依赖
<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger2 -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>

3.3 编写hello工程
写了一个hello请求之后 就有两个请求了 还有一个是默认得我 /error请求--->Whitelabel Error Page
3.4 老套路 需要编写相应的SwaggerConfig类把Swagger放入容器中
### 总结下 Component 有哪些子注解 @Controller(控制器层) @Service(服务层) @Repository(数据持久层) @Configuration(配置类)
老套路:
@Configuration
@EnableSwagger2 //开启Swagger2  
先什么都不写(什么都不配置,使用默认的配置,[开启Swagger @Configuration注入到Spring容器中])====> 
[测试运行] 访问 localhost:8080/虚拟项目路径/swagger-ui.html
### swagger-ui.html这个页面在哪里:去[swagger-ui]的包下的resource找
· 页面初步分析 4个部分: 
  Models 实体类信息
  Controllers 接口信息
  API Documentation Swagger信息 
  select a spec 组(存在默认组default)
3.5 在[详细注释见SwaggerConfig.java]配置Swagger 
·配置基本信息
    Swagger的bean实例===[Docket] 点进Docket类 构造函数 看他需要哪些参数进行配置
    需要传入一个 documentationType 点进查看什么是documentationType
    需要设置ApiInfo直接点进去看构造函数 或者静态区
· 配置自定义扫描接口(默认扫描所有的接口)
  使用docket的select()方法 [链式操作]
@Bean
public Docket getDocket(){
    // 2.设置 docket
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(getApiInfo())
            .enable(flag)
            [.select()]
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
            [.build()]; // 建造者模式
### select().......build() 是一整套 不能再中间穿插跟api配置无关的配置
· 配置是否启动Swagger--Docket对象里有个enable方法
new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).enable(false)
====>[设置为false的话 swagger就不能再浏览器中访问]
3.6 需求:只希望Swagger在开发环境中使用 而不再生产环境中使用
· 在application.yaml中定义两套环境 dev 和 prod 环境
· 在上述的配置环境中[读取当前的环境]
// 1.配置Swagger的Docket bean实例 注入到容器中
@Bean
public Docket getDocket(Environment environment){
    // 3. 设置哪些环境下需要显示swagger
    // 3.1 获取当前项目的环境(被激活的那个) 当前的环境是否是下面其中之一
    Profiles profiles = Profiles.of("dev","test","...");
    // 3.2 这里监听Profiles 需要一个Profile对象 --- 环境变量实例
    // 判断当前环境中的profiles是不是上面定义的那几个profiles之一 返回boolean值
    boolean flag = environment.acceptsProfiles(profiles);
    // 最后再把是否开启Swagger的标志flag传入设置
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(getApiInfo())
            .enable(flag)

3.6 配置api文档的分组
Docket类中有个属性叫groupName
通过return new Docket(DocumentationType.SWAGGER_2).[groupName("groupName")]进行设置
创建多个分组就是定义多个 Docket 即可

3.7 实体类的配至(Model)
[只有在接口中(也就是加了@XxxMapping注解的方法) 返回值存在的实体类才会被Swagger扫描到]
·在HelloController添加一个返回User实体类的接口([一定要是@RestController或是@ResponseBody才行])
·常用加说明文字的注解===>给一些比较难理解的属性或者接口增加注释信息
@Api 类 ==== > 不过好像没什么用
@ApiModel 实体类上 
@ApiModelProperties 实体类的属性上
@ApiOperation 接口上(Controller的方法上)
@ApiParam 接口的参数上(Controller的方法的参数上)
[实体类里只有public的属性才会展示在swagger-ui里面 private的属性不会展示出来]

3.7 使用Swagger进行API接口的在线测试
点开Controller列表 -- > 点击想测试的API上方的try it out --> 输入需要的参数
--> 点击execute按钮 -->检查返回值 [错误信息]等等 
### 注意点:正式发布的时候要关闭Swagger!!! 安全、节省运行内存