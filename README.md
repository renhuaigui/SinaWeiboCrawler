SimpleWeiboCrawler
==================

新浪微博爬虫


描述
=================
数据库目前只有三张表，建表sql附在文件夹里了。

之后在Config.java里配置数据库IP、端口、名称、用户和密码。另外，THREAD_SLEEP是两次抓取间隔时间，防止速度过快受到新浪限制；

UID是指定抓取指定用户ID；START_DATE和END_DATE是抓取微博内容的开始和结束时间。

如果cookie失效了，可以把IS_LOGIN改成true。运行后会自动弹出一个微博页面，在弹出的页面的任意位置手动登录，大约20秒后会页面自动关闭，程序便可以获取cookie并继续运行。

也可以单独运行com.weibo.util包下的SeleniumClient.java，将输出的cookie替换Config.java里的cookie，然后将IS_LOGIN改成false。
**注：此步需要firefox支持。


微博内容根据搜索结果逐页抓取，每页分为三部分，第一部分是一开始显示的15条，第二、三部分对应页面两次滚动到底部各次更新的15条。
关注和粉丝每页抓取20条，抓完10页后即200条后有可能会受到新浪限制。目前解决办法是每10页加一个额外延时，以后考虑增加多账号或多IP解决。
抓取二级关注和二级粉丝耗时较长，测试的时候可以在Main.java中把相应的两行注释掉或者在com.weibo.follow中FollowCraler.java的81行for循环中限制每位用户抓取的页面数量或者在FollowCraler.java中followFollow、fanFan两个方法的for循环中增加抓取的二级用户数量限制。




