# vue-spring boot 搭建的学院网站

这是一个前后端分离的网站，采用单页web，前端用的vue2，后端用spring boot2，数据库用的MySQL8.0。

# 目录结构
- szweb-back文件夹是后端代码
- szweb-client文件夹是前端代码

# 运行步骤

1. 新建一个空的数据库szweb
2. 打开后端代码，在配置文件application.properties里修改成自己电脑MySQL的用户名和密码，F5运行后端
3. 在szweb数据库里面插入sql文件里面的数据
4. 打开前端代码，在终端运行npm run serve，后端端口是8000，前端是8080，访问http://localhost:8080/ 即可看到前端页面。
