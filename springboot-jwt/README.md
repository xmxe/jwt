测试:
1. 访问`http://localshost:8080` 返回
~~~json
{
    "content": "hello freewolf~"
}
~~~
未登录状态下访问`http://localshost:8080/hello` ,`http://localshost:8080/world都返回`
~~~json
{
    "timestamp": "2022-05-20T06:03:38.402+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "",
    "path": "/hello"
} 
,
{
  "timestamp": "2022-05-20T06:03:51.966+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "",
  "path": "/world"
}
~~~
2. post访问`http://localshost:8080/login` ,设置`contentType/json {"username":"admin","password":"123456"}`返回
~~~json

{
    "result": "eyJhbGciOiJIUzUxMiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfQURNSU4sQVVUSF9XUklURSIsInN1YiI6ImFkbWluIiwiZXhwIjoxNjUzNDU4ODc4fQ.GYNnK-uFM1l3a6wThfYbRm847TB0EzS7VoHf0Y5eSlNJ1NHELQUeFGE4DtKsOKBB0t108Ew4NbwHqw6OfL7MtQ",
    "message": "",
    "status": 0
}
~~~
3. 请求头增加Authorization:"Bearer token字符串" 然后访问`http://localhost:8080/users` 返回
~~~json
{
    "result": [
        "freewolf",
        "tom",
        "jerry"
    ],
    "message": "",
    "status": 0
}
~~~
访问成功！