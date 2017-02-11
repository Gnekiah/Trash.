#blogdemo
*based on tornado*. 动态请求通过nginx映射到python应用，静态请求直接返回静态文件。

### files
templates html模板  
static 静态文件位置  
static/background 用于在网页上显示图片  
static/files 上传文件的目录  
static/cache 缓存目录  
static/blog blog内容、索引以及数据库文件目录  
static/cland cland内容、索引以及数据库文件目录  
log.log 日志文件  
config.conf 配置文件，配置登录名、密码、哈希盐值、cookie序列等

### router
IndexHandler 主页    
BlogHandler 博客展示页   
ArticleHandler 博客文章浏览页  
ClandHandler 预留X展示页  
ChapterHandler 预留X浏览页  
AboutHandler about  
LoginHandler 登录  
LogoutHandler 登出  
ManageHandler 管理  
WritingHandler write blog or add cland page  
UploadImageHandler 上传图片  
UploadHandler 上传内容   
通过在TODO中添加自己的功能，以及自建html页实现网站功能

### markup语法
实现markdown的部分功能。~~用了一种很傻缺的实现方式~~

    #大标题
    ##二级标题
    
    >缩进
    >>二级缩进
    
    ?? 
        代码段
    ?!
    
    0?
    0. 第一项 有序
    0. 第二项
    0. 第三项
    0!

    *?
    *. 第一项 无序
    *. 第二项
    *. 第三项
    *!
    
    ==== 分割线
    ![tupian](C:/9105080.jpg)
    %%粗体%%
    ^^斜体^^
    $$划线体$$
    ``ctrl+a``
    @@google^https://google.com@@
    

        
            
