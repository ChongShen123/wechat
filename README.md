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
  "token":"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJza3kiLCJjcmVhdGVkIjoxNTc3MjU4NjAzNDQ5LCJleHAiOjE5MzcyNTg2MDN9.iXo5JdtVXKO48JvCPC9ulgTtcNPLYWQdZLzI8qWKur0jjpF7OBEp_IcQ5fX-oHBoSGEywx6h75PVl0_MAFs3Iw",
  "user_id":7,
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
## 1002 单聊
发送者

请求
```
{
    "cmd":1002,
    "toUserId":20,
    "content":"hello",
    "type":0
}
```
响应
```
{
  "code":2000,
  "message":"ok",
  "type":1002
}
```
接收者
```
{
  "code":2000,
  "data":{
    "content":"hello",
    "createTimes":1577352857101,
    "fromUserId":7,
    "id":"1210131708408119296",
    "read":true,
    "toUserId":20,
    "type":0
  },
  "message":"ok",
  "type":1002
}
```
> type : 0信息 1语音 2图片 3撤销 4 加群 5退群 6红包 7转账
> read: 是否阅读
>
> 说明:为减少数据传输量,这里消息只包含fromUserId.在展示时建议先获取用户所有好友的基本信息缓存到一个Map中.当需要展示用户昵称,和头像时可根据fromUserId在缓存map中获取.



## 1003 撤销消息

**发送者**

请求

```
{
  "cmd":1003,
  "id":"1210141165284376576",
  "toUserId":20
}
```

响应--成功

```
{
  "code":2000,
  "message":"ok",
  "type":1003
}
```

响应--失败

```
{
  "code":4029,
  "message":"操作失败！已经超过撤销时间"
}
```

**接收者**

```
{
  "code":2000,
  "data":{
    "createTimes":1577355353416,
    "id":"1210142127893921792"
  },
  "message":"ok",
  "type":1003
}
```

> id 为要撤消的消息id

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

## 3000 创建群聊

请求

```
{
  "cmd":3000,
  "ids":"20"
}
```

> ids: 用户id  多个用户 用逗号隔开

成功响应

```
{
  "code":2000,
  "data":{
    "content":"您已加入【sky、sky2的群聊】 开始聊天吧",
    "createTimes":1577360650794,
    "fromUserId":0,
    "id":"1210164397525843969",
    "toUserId":7,
    "type":4
  },
  "message":"ok"
}
```

> 该消息会发送给被邀请加入群聊的所有用户
>
> fromUserId : 0  0为系统通知
>
> type: 0信息 1语音 2图片 3撤销 4 加入群聊 5退群 6红包 7转账

失败响应

```
{
  "code":4003,
  "message":"没有相关权限",
  "type":3000
}
```

> 只有管理员可以创建群聊

## 3001 查看用户所有群

请求

```
{
  "cmd":3001
}
```

响应

```
{
  "code":2000,
  "data":{
    "count":1,
    "groupList":[
      {
        "gid":66,
        "groupName":"sky、sky2的群聊",
        "icon":"group/护眼模式_20191109094558.jpg",
        "noticeType":true
      }
    ]
  },
  "message":"ok",
  "type":3001
}
```

> noticeType: 是否强行通知

## 3002 查看群基本信息

请求

```
{     
	"cmd":3002,
	"group_id":66 
}
```

响应

```
{
  "code":2000,
  "data":{
    "addFriendType":true,
    "id":66,
    "membersCount":2,
    "name":"sky、sky2的群聊"
  },
  "message":"ok",
  "type":3002
}
```

> membersCount: 群成员个数
>
> addFriendType: 群成员是否可互加好友

## 3003 查看群详情信息

请求

```
{     
	"cmd":3003,
	"group_id":66 
}
```

响应

```
{
  "code":2000,
  "data":{
    "addFriendType":true,
    "createTimes":1577360650010,
    "icon":"group/护眼模式_20191109094558.jpg",
    "id":66,
    "membersCount":2,
    "name":"sky、sky2的群聊",
    "notice":"这是群简介",
    "qr":"qr/2019/12/26/group_661577360650216.png"
  },
  "message":"ok",
  "type":3003
}
```

> qr: 群二维码

## 3004 加入群聊

**邀请者**

请求

```
{
    "cmd":3004,
    "ids":"21",
    "group_id":66
}
```

> ids: 被邀请者id,如果有多个用户,用逗号隔开

响应--成功

```
{
  "code":2000,
  "message":"ok",
  "type":3004
}
```

响应--失败

```
{
  "code":4023,
  "message":"加群失败！用户已加入群聊",
  "type":3004
}
```

**被邀请者**

响应

```
{
  "code":2000,
  "data":{
    "content":"您已加入【sky、sky2的群聊】 开始聊天吧",
    "createTimes":1577440271983,
    "fromUserId":0,
    "id":"1210498352997351424",
    "toUserId":21,
    "type":4
  },
  "message":"ok"
}
```

> fromUserId:0, 系统通知
>
> type:4 入群通知

**该群在线成员**

响应

```
{
  "code":2000,
  "message":"sky3已加入群聊",
  "type":3004
}
```

## 3005 查看群所有用户

请求

```
{
	"cmd":3005,
	"group_id":66 
}

```

响应

```
{
  "code":2000,
  "data":[
    {
      "groupNickname":"sky2",
      "icon":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm7O1QZxOj38DFs9pFHwX3it2CiCMXiVMUPOXI337CKfRhFaYVaw&s",
      "uid":20
    },
    {
      "groupNickname":"sky",
      "icon":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm7O1QZxOj38DFs9pFHwX3it2CiCMXiVMUPOXI337CKfRhFaYVaw&s",
      "uid":7
    },
    {
      "groupNickname":"sky3",
      "icon":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm7O1QZxOj38DFs9pFHwX3it2CiCMXiVMUPOXI337CKfRhFaYVaw&s",
      "uid":21
    }
  ],
  "message":"ok",
  "type":3005
}
```

## 3006 群聊

请求

```
{
  "cmd":3006,
  "group_id":66,
  "content":"hello",
  "type":0
}
```

> type: 0信息 1语音 2图片 3撤销 4 加群 5退群 6红包 7转账

响应

```
{
  "code":2000,
  "data":{
    "content":"hello",
    "createTimes":1577440912128,
    "fromUserId":7,
    "id":"1210501037960085504",
    "toGroupId":66,
    "type":0
  },
  "message":"ok",
  "type":3006
}
```

> 群中所有在线用户都将收到该消息
>
> 说明:为减少数据传输量,这里消息只包含fromUserId.在展示时建议先获取本群所有在线用户缓存到一个Map中.当需要展示用户昵称,和头像时可根据fromUserId在缓存map中获取.

## 3007 移除群聊

**操作者**

请求

```
{
    "cmd":3007,
    "group_id":66,
    "ids":"21"
}
```

> ids: 多个用户用逗号隔开

响应---成功

```
{
  "code":2000,
  "message":"ok",
  "type":3007
}
```

响应---失败

```
{
  "code":4004,
  "message":"参数检验失败",
  "type":3007
}
```

> 如果被移除用户已不在该群组,则返回参数检验失败.

**该群在线用户**

响应

```
{
  "code":2000,
  "data":{
    "count":2,
    "message":"sky已将sky3移出群聊"
  },
  "message":"ok",
  "type":3007
}
```

> count: 当前群人数

**被移除者**

响应

```
{
  "code":2000,
  "data":{
    "content":"您已退出【sky、sky2的群聊】群聊",
    "createTimes":1577442842098,
    "fromUserId":0,
    "id":"1210509132840976384",
    "toUserId":21,
    "type":5
  },
  "message":"ok"
}
```

> fromUserId:0 系统通知
>
> type: 0信息 1语音 2图片 3撤销 4 加群 5退群 6红包 7转账

## 3008 退出群聊

**操作者**

请求

```
{
  "cmd":3008,
  "group_id":66,
  "ids":"21"
}
```

响应---成功

```
{
  "code":2000,
  "message":"ok",
  "type":3008
}
```

响应---失败

```
{
  "code":4024,
  "message":"操作失败！用户不在该群组",
  "type":3008
}
```

**该群在线用户**

响应

```
{
  "code":2000,
  "data":{
    "count":2,
    "message":"sky3已退出群聊"
  },
  "message":"ok",
  "type":3008
}
```

> count: 群成员数