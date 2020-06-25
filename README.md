# 在线多人博客平台
这是基于 Spring Boot 的多人在线博客平台的后端实现,主要包括注册登录模块,博客模块以及相关的测试模块。
可以基于该后端实现开发对应的前端博客实现(当然地，该仓库包含一个基础的前端页面).

## Feature
+ 本项目主要基于TDD开发模式，基本思路就是通过测试来进行小步开发从而推动整个开发的进行
+ 其中主要对Service层，Controller层主要用Junit以及Mockito库进行单元测试
+ 导入相关的集成库完成整合的集成测试
+ 在 Maven 的 verify 周期绑定了 Checkstyle 和 SpotBugs 插件，保证代码质量
+ 配置 travisCI 实现对 Gtihub 项目的自动化测试(每次pull request都进行自动化测试)
+ 使用了 docker 启动本项目及 MySQL 数据库
+ 使用了 Spring Security 框架完成登录模块 
+ 使用了 Mybatis-plus 框架完成对`users`及`blogs`表的增删查改

## Preview
+ [整体项目预览](https://jirengu-inc.github.io/vue-blog-preview/)
+ [后端接口文档](interface-doc.md)
+ [前端代码仓库](https://github.com/jirengu-inc/vue-blog-preview)

## Requirements
+ jdk1.8
+ maven
+ docker

## Installation
+ 运行Application类方案:
 
 clone 项目至本地目录：

```shell
git clone https://github.com/NervousOrange/Multiplayer-Online-Blog-Platform.git
```

 从 Docker 启动 MySQL 数据库：
    + [Docker 下载地址](https://www.docker.com/)
    + 如果需要持久化数据需要配置 -v 磁盘文件映射

```shell
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123 -d mysql:5.7.27
```

 使用 IDEA 打开项目，刷新 Maven，再使用开源数据库迁移工具 Flyway 完成自动建表工作：

```shell
mvn flyway:clean && mvn flyway:migrate
```


+ 另一种方案

创建 `blog-park`镜像

```shell
docker build . -t blog-park
```

在config/application.yaml设置ip
    
```yaml
spring:
  # datasource 数据源配置内容
  datasource:
    # 替换自己的ip(如:192.168.77.1)
    url: jdbc:mysql://192.168.77.1:3306/blogpark?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: 123
```

运行Docker
```shell
docker run -it -p 8081:8080 -v D:/blog-park/config/application.yaml:/app/application.yaml blog-park
```

+ 运行项目
    + 访问测试接口查看[后端接口文档](interface-doc.md)
    + 浏览整体项目访问 [localhost:8080/index.html](localhost:8080/index.html)
