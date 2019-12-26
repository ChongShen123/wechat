[toc]
# 命令报文

## 1000 心跳

请求

```
{
  "cmd":1000
}
```

响应

```
{
  "code":2000,
  "message":"ok",
  "type":1000
}
```

## 1001 注册聊天服务器
请求
```
{
  "token":"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJza3kiLCJjcmVhdGVkIjoxNTc2NDEwMDYzMjY4LCJleHAiOjE5MzY0MTAwNjN9.UyGh7kRwGqKWQU1XkpsOsk4-3ufUtQssOO3kYCNW-kGQZs3DuqrAj6hoHwbS5wfRf66Wo8zvcNfgv8o7o_IUVA",
  "cmd":1001
}
```

响应

```
{
  "code":2000,
  "message":"您已连接成功!",
  "type":1001
}
```

## 2000 添加好友

请求

```
{
    "username":"sky",
    "content":"hello",
    "cmd":2000
}
```

响应

```
{
  "code":2000,
  "message":"发送成功",
  "type":2000
}
```



被添加的好友会收到

```
{
  "code":2000,
  "data":{
    "createTimes":1577259790334,
    "fromUserIcon":"img/default6.png",
    "fromUserId":25,
    "fromUsername":"sky5",
    "id":"1209741358095024128",
    "message":"hello",
    "read":false
  },
  "message":"ok",
  "type":2000
}
```

## 2001 同意好友

请求

```
{
  "id":"1209744134367756288", 
  "state":2, 
  "cmd":2001
}
```

> id: 消息 id
> state: 0 未处理1 同意 2 拒绝 

申请好友者会收到消息

```
{
  "code":2000,
  "data":{
    "createTimes":1577259158029,
    "fromUserIcon":"img/default7.png",
    "fromUserId":23,
    "fromUsername":"sky6",
    "message":"我们已经是好友啦！",
    "read":true
  },
  "message":"ok",
  "type":2001
}
```

重复操作

```
{
  "code":4019,
  "message":"操作失败！请不要做重复的操作",
  "type":2001
}
```



## 2002 查询好友申请表

请求

```
{
    "cmd":2002
}
```

响应

```
{
  "code":2000,
  "data":[
    {
      "createTimes":1577258829020,
      "fromUserIcon":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm7O1QZxOj38DFs9pFHwX3it2CiCMXiVMUPOXI337CKfRhFaYVaw&s",
      "fromUserId":7,
      "fromUsername":"sky",
      "id":"5e030f4d023c9b3fed5a074a",
      "message":"hello",
      "read":true, // 是否阅读
      "state":0
    }
  ],
  "message":"ok",
  "type":2002
}

```

> state: 0 未处理1 同意 2 拒绝 



## 2004 删除好友

请求

```
{
  "friend_id":23,
  "cmd":2004
}
```

响应

```
{
  "code":2000,
  "message":"ok",
  "type":2004
}
```

