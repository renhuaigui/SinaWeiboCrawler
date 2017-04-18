SimpleWeiboCrawler
==================

新浪微博爬虫


描述
=================
数据库目前只有三张表，content,image,user

之后在Config.java里配置数据库IP、端口、名称、用户和密码。另外，THREAD_SLEEP是两次抓取间隔时间，防止速度过快受到新浪限制；
同时配置用户登录cookie

UID是指定抓取指定用户ID；START_DATE和END_DATE是抓取微博内容的开始和结束时间。

如果cookie失效了，可以把IS_LOGIN改成true。运行后会自动弹出一个微博页面，在弹出的页面的任意位置手动登录，大约20秒后会页面自动关闭，程序便可以获取cookie并继续运行。

也可以单独运行com.weibo.util包下的SeleniumClient.java，将输出的cookie替换Config.java里的cookie，然后将IS_LOGIN改成false。
**注：此步需要firefox支持。

Main.java根据提供的微博用户id来爬去用户信息，以及带图微博.
getImage.java根据得到的图片地址批量下载图片存储到本地




