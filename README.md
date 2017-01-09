# Dubbo-Spring-Mybatis-Generator

## Ability (quickly build Dubbo-Spring-Mybatis rpc project in one minute)
    - 快速生成spring-boot-duboo-rpc 服务框架模板代码 拒绝copy代码带来的坑 提高开发效率
    - 生成的代码目录结构 风格 命名 配置方式等都是统一的 减少代码阅读和配置的负载 方便团队成员沟通
    - (整合MBG)快速生成Dao ORM层 模板代码，只要定义表结构，就可以生成对应的mybatis 配置 Dao mapper xml model相关代码
    - 根据表结构或配置生成Service 层接口及对应的简单实现
    - 根据表结构或cgi配置生成相应的Dto 前后端定义好协议 即可生成相关pojo对象
    - 根据Pojo,Dto 对象自动生成DtoConvertUtil工具类 避免写烦人的get set
    - 项目生成的配置(pom 依赖配置，logback配置,application.propertis配置等)可以复用以前项目的模板 通过修改配置参数传进来
    - 生成对应的service 层单元测试框架配置
    - 每项功能基本上可以独立使用 在已有项目平时开发过程中也可以省一些体力活
    - 开箱即用 生成完的项目可以不用修改即可启动一个服务
## Output

生成的单个rpc目录结构如下
    - api层(service api interface)
    ![](https://coding.net/u/chunxiao/p/project-generator/git/raw/master/api.png)
    - service 实现层(provider)
    ![](https://coding.net/u/chunxiao/p/project-generator/git/raw/master/provider.png)

生成的单个web目录结构如下
![](https://coding.net/u/chunxiao/p/project-generator/git/raw/master/web.png)

生成所有目录结构如下
![](https://coding.net/u/chunxiao/p/project-generator/git/raw/master/all.png)

## Usage
- step1. 安装JDK8(框架代码用了J8,生成的项目不要求)
- step2. 下载[hamrmer.jar](http://10.8.15.73/dafy_dev/project-generator/raw/master/out/harmer.jar)
- ste3. java -jar harmer.jar -f config.yaml
- setp4.  进入生成好的目录 mvn install 然后执行启动服务即可

## Options
    生成config sample文件 : java -jar harmer.jar y -t rpc
    -t [rpc|web|one_key] 生成配置的类型 不同项目配置项不一样
    -p /opt/config    生成配置的输出目录

    生成项目框架文件 : java -jar harmer.jar -f /opt/config、config.yaml
    -f:指定生成的配置文件路径(绝对路径)

## Configuration
**!!注意 配置文件是yaml格式  [在线编辑yaml](http://codebeautify.org/yaml-validator)  完整配置及默认值请参考源码 [config.yaml](http://10.8.15.73/dafy_dev/project-generator/raw/master/src/main/resources/template/all_config.yaml)**
![](https://coding.net/u/chunxiao/p/project-generator/git/raw/master/config.png)
## Bugs
   写的比较匆忙 测试不是很充分 边界条件的bug估计不少
   MBG mybatis 会对定义成text等大字段生成继承结构的pojo 会有一些小bug

## RoadMap
    - 支持java7
    - 支持spring (not only spring-boot)
    - 根据接口生成对应的单元测试
    - 支持自定义dao接口方法名
    - 图形化配置操作



