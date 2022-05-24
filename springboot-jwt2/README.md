- [spring security + jwt](https://mp.weixin.qq.com/s/dk8CW5uvMPD-KE7ruaqwmA)

--- 
测试：
访问http://localhost:8080/login 获取token
请求头携带Authentication:token
访问http://localhost:8080/t1 返回结果，否则返回403
