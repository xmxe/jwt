测试:
1. 请求http://localhost:8082/hello 返回
~~~json
{
    "timestamp": "2022-05-20T05:31:01.745+00:00",
    "status": 401,
    "error": "Unauthorized",
    "message": "",
    "path": "/hello"
}
~~~
2. post请求http://localhost:8082/authenticate 设置contentType/json {"username":"javainuse","password":"password"} 返回
~~~json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE2NTMwNDI5NTEsImlhdCI6MTY1MzAyNDk1MX0.W4yhxbCkuhqvj55VZ1FCboupPSodkp1KI4NtU2lDHTLldLoh9rsUPnmdjzB8k6EaSuFSGnafGeZrBfj5gV7XCQ"
}
~~~
3. 请求头增加Authorization:"Bearer token字符串" 然后访问http://localhost:8082/hello,返回
~~~
Hello World
~~~