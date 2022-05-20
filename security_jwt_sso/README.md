搬运自 https://github.com/Oxygen404/Springsecurity

--- 
#### Springsecurity 单点登录SSO解决方案之SpringSecurity+JWT实现

~~~ 
参考博客：https://blog.csdn.net/qq_38526573/article/details/103409430
原博主没有分享完整源码，这是我按照博客自己写的，供大家参考

数据库的表名为t_user,字段分别是username 和 password
其中password不能直接存明文，需要存储BCryptPasswordEncoder加密后的字符，可以直接调用encode方法进行加密
~~~ 

- [Spring Security + JWT 实现单点登录，还有谁不会？？](https://mp.weixin.qq.com/s/5xt0i7Bwj4-Io-lYDkySug)
