搬运自 https://github.com/Oxygen404/Springsecurity

--- 
#### Springsecurity单点登录SSO解决方案之SpringSecurity+JWT实现

>参考博客：https://blog.csdn.net/qq_38526573/article/details/103409430 原博主没有分享完整源码，这是我按照博客自己写的，供大家参考。
>数据库的表名为user,字段分别是username和password。其中password不能直接存明文，需要存储BCryptPasswordEncoder加密后的字符，可以直接调用encode方法进行加密

--- 

测试：
1. 未登录状态下请求资源服务器的资源
访问http://localhost:8083/query
~~~json
{
    "timestamp": "2022-05-20T07:00:08.926+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "",
    "path": "/query"
}
~~~
访问http://localhost:8083/update
~~~json
{
    "timestamp": "2022-05-20T07:00:57.479+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "",
    "path": "/update"
}
~~~
返回403
2. 请求认证服务器，根据用户名密码生成token并返回
post访问http://localhost:8081/login 设置contentType/json {"username":"jwt","password":"jwt"} 返回
~~~json
{
    "msg": "认证通过！",
    "code": 200
}
~~~
查看返回的响应头
~~~
Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoie1wiaWRcIjpudWxsLFwidXNlcm5hbWVcIjpcImp3dFwiLFwicGFzc3dvcmRcIjpudWxsLFwic3RhdHVzXCI6bnVsbCxcInJvbGVzXCI6W3tcImF1dGhvcml0eVwiOlwiQURNSU5cIn1dfSIsImp0aSI6IllXUmtPV05pWkRjdFl6ZzJNUzAwWTJNeUxUbGpOVE10T1RJMk1EQTRPR0pqT1RVNSIsImV4cCI6MTY1MzExNzA1NX0.efmeZbmZFHxX5g9FU3E_Php2tBi4RUZyfrpTRUB51PYaZigy9QXWqoWb-22MyPJpQBOiGIne3aNlxJhnsHPBvsIOokLvQSzrJas_LolMogEbXapFG2BIRPQiaCAiADwUowj0wbsWpxHVuZTMDPgSx3TNRB2qUOdwhK4lMQ5D0_C0ZG7HoRzmBXbuSyUlQAcfD7-79jTWWD2DGlsfE4IE0I1BFEsIdZ5CEX3pMwuViBIcAVYZX9p97Ag4Yy1F8G3pWJcdAnWLbPg-kzEiFppJ5Ysdh2CZpfy9b1Vn17X0aPpT562Dy3jnHfU6IU9ylecWxWVb0ACrUePg-hhiZd7LhA
~~~
3. 访问http://localhost:8083/update 请求头携带响应头返回的内容
~~~
Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJ1c2VyIjoie1wiaWRcIjpudWxsLFwidXNlcm5hbWVcIjpcImp3dFwiLFwicGFzc3dvcmRcIjpudWxsLFwic3RhdHVzXCI6bnVsbCxcInJvbGVzXCI6W3tcImF1dGhvcml0eVwiOlwiQURNSU5cIn1dfSIsImp0aSI6IllXUmtPV05pWkRjdFl6ZzJNUzAwWTJNeUxUbGpOVE10T1RJMk1EQTRPR0pqT1RVNSIsImV4cCI6MTY1MzExNzA1NX0.efmeZbmZFHxX5g9FU3E_Php2tBi4RUZyfrpTRUB51PYaZigy9QXWqoWb-22MyPJpQBOiGIne3aNlxJhnsHPBvsIOokLvQSzrJas_LolMogEbXapFG2BIRPQiaCAiADwUowj0wbsWpxHVuZTMDPgSx3TNRB2qUOdwhK4lMQ5D0_C0ZG7HoRzmBXbuSyUlQAcfD7-79jTWWD2DGlsfE4IE0I1BFEsIdZ5CEX3pMwuViBIcAVYZX9p97Ag4Yy1F8G3pWJcdAnWLbPg-kzEiFppJ5Ysdh2CZpfy9b1Vn17X0aPpT562Dy3jnHfU6IU9ylecWxWVb0ACrUePg-hhiZd7LhA
~~~
接口返回
~~~
update
~~~
成功！
