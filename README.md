[toc]
# 命令报文

## 基本

### 1000 心跳

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
### 1001 注册聊天服务器
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
### 1002 单聊
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



### 1003 撤销消息

**说明**

20s内的消息可以发送撤销消息.

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

## 好友

### 2000 添加好友

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
### 2001 同意好友
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
### 2002 查询好友申请表
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
### 2004 删除好友
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

### 2005 查询好友详情

请求

```
{
  "cmd":2005,
  "user_id":20
}
```

响应

```
{
  "code":2000,
  "data":{
    "email":"2928448688@qq.com",
    "gender":0,
    "icon":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm7O1QZxOj38DFs9pFHwX3it2CiCMXiVMUPOXI337CKfRhFaYVaw&s",
    "id":20,
    "lastLoginTimes":1577704602566,
    "loginState":true,
    "qr":"qr/2019/12/12/sky21576132284724.png",
    "uno":123123123
  },
  "message":"ok",
  "type":2005
}
```

> loginState:好友登录状态, true 在线 ,false 离线

## 群组

### 3000 创建群聊

**说明**:  只有管理员可以创建群聊

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

### 3001 查看用户所有群

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

### 3002 查看群基本信息

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
    "membersCount":3,
    "name":"sky、sky2的群聊",
    "noSayType":0
  },
  "message":"ok",
  "type":3002
}
```

> membersCount: 群成员个数
>
> addFriendType: 群成员是否可互加好友
>
> noSayType: 群是否开启聊天功能 0禁止发言 1 允许发言

### 3003 查看群详情信息

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
    "membersCount":3,
    "name":"sky、sky2的群聊",
    "noSayType":0,
    "notice":"这是群简介",
    "qr":"qr/2019/12/26/group_661577360650216.png"
  },
  "message":"ok",
  "type":3003
}
```

> qr: 群二维码
>
> addFriendType 是否允许群内用户互加好友
>
> noSayType: 群是否开启聊天功能 0禁止发言 1 允许发言

### 3004 加入群聊

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

### 3005 查看群所有用户

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

### 3006 群聊

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

### 3007 移除群聊

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

### 3008 退出群聊

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

### 3009 解散群组

**说明**:只有群主可以解散群组

请求

```
{
    "cmd":3009,
    "group_id":75
}
```

响应--成功

```
{
  "code":2000,
  "message":"sky、sky2的群聊群组已解散."
}
```

响应--失败

```
{
  "code":4003,
  "message":"没有相关权限",
  "type":3009
}
```

### 3010 禁言群聊用户

**说明**: 只有群管理员可以对本群

请求

```
{
  "cmd":3010,
  "group_id":66,
  "user_id":"7",
  "times":-1
}
```

> user_id 被禁言用户id
>
> times: 禁言类型分3种  -1永久禁言,1开启发言,大于当前时间戳(毫秒时间戳)为禁言多少时间.



被禁言用户发言群聊时的响应

响应 --- 类型1 永久禁言

```
{
  "code":4030,
  "message":"您已被禁止发言",
  "type":3006
}
```

> 3006 会群聊类型消息

响应 --- 类型2 指定禁言时间

```
{
  "code":4000,
  "data":{
    "relieveTimes":10234
  },
  "type":3006

```

> relieveTimes: 为解除禁言的剩余毫秒时间戳

### 3011 添加或移除群管理员

请求

```
{
  "cmd":3011,
  "group_id":66,
  "type":1,
  "user_id":"20"
}
```

> type: 1添加管理员,2移除管理员

响应---成功

```
{
  "code":2000,
  "message":"ok",
  "type":3011
}
```

响应---失败1 重复添加

```
{
  "code":4019,
  "message":"操作失败！请不要做重复的操作",
  "type":3011
}
```

响应---失败2 重复删除

```
{
  "code":4020,
  "message":"操作失败！数据不存在",
  "type":3011
}
```

### 3012 开启或关闭群聊天功能

只有群管理员可以进行设置

请求

```
{
  "cmd":3012,
  "group_id":66,
  "type":0
}
```

> type: 0关闭 1开启

响应

```
{
  "code":2000,
  "message":"ok",
  "type":3012
}
```



若群关闭聊天功能,群聊时返回响应

```
{
  "code":4032,
  "message":"该群已关闭聊天功能",
  "type":3006
}
```

> 判断该群是否可进行群聊. 可从缓存该群基本信息的 noSayType 来判断.而不必让群成员每次发送信息到服务端,让服务端进行判断,从而减少客户端与服务端的交互.

### 3013 修改群信息

只有群管理员可以修改群信息

请求

```
{
  "cmd":3013,
  "group_id":66,
  "group_name":"测试的群聊",
  "icon":"20191017133503.jpg",
  "notice":"这是本群的最新公告哦"
}
```

> icon 为群头像地址. 如果地址不存在则修改失败.

响应

```
{
  "code":2000,
  "message":"ok",
  "type":3013
}
```

### 3014 设置群相互添加好友    

**说明**: 只有群管理员可以进行设置

请求

```
{     
	"cmd":3014,     
	"group_id":77,     
	"add_friend":true 
}

```

> add_friend: true -> 群用户可相互添加好友. false -> 群用户不可相互添加好友.

响应

```
{
  "code":2000,
  "message":"ok",
  "type":3014
}
```

## 用户

### 4000 查询用户详情

**说明**: 只有对应平台的管理员可以查看对应平台的会员详情

请求

```
{
    "cmd":4000,
    "user_id":7
}
```

响应

```
{
  "code":2000,
  "data":{
    "email":"2928448687@qq.com",
    "gender":0,
    "icon":"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQm7O1QZxOj38DFs9pFHwX3it2CiCMXiVMUPOXI337CKfRhFaYVaw&s",
    "id":7,
    "lastLoginTimes":1577670607122,
    "qr":"qr/2019/12/11/sky1576061378229.png",
    "type":1,
    "uno":413413413,
    "username":"sky"
  },
  "message":"ok",
  "type":4000
}
```

> qr: 二维码
>
> uno: 系统账号
>
> gender: 性别 0女 1男
>
> type: 会员类型  0会员  1管理员

### 4001 查看用户金额

请求

```
{
    "cmd":4001
}
```

响应

```
{
  "code":2000,
  "data":100,
  "message":"ok",
  "type":4001
}
```

> data:为用户金额

### 4002 用户转账

请求

```
{
    "cmd":4002,
    "toUserId":42,
    "price":100,
    "content":"这是给你的礼物"
}
```

> content : 为转账消息,可以不传.

响应

```
{
  "code":2000,
  "message":"ok"
}
```

**接收用户**

响应--- 1

```
{
  "code":2000,
  "data":{
    "content":"这是给你的礼物!",
    "createTimes":1578124153843,
    "fromUserId":7,
    "id":"1213366761418276864",
    "read":true,
    "toUserId":42,
    "type":7
  },
  "message":"ok",
  "type":4002
}
```

响应 --- 2

```
{
  "code":2000,
  "data":{
    "content":"来自sky的转账信息,
    请注册查收!",
    "createTimes":1578123799624,
    "fromUserId":7,
    "id":"1213365275716108288",
    "read":true,
    "toUserId":42,
    "type":7
  },
  "message":"ok",
  "type":4002
}
```

# 系统通知

## 用户充值通知

```
{
  "code":2000,
  "data":{
    "content":"系统已为您充值200元,
    请注意查看钱包余额!",
    "createTimes":1578032722402,
    "fromUserId":0,
    "id":"1212983270159564800",
    "read":false,
    "toUserId":7,
    "type":6
  },
  "message":"ok",
  "type":1002
}
```

## 用户提现通知

```
{
  "code":2000,
  "data":{
    "content":"您已提现100元,
    请注意查收!",
    "createTimes":1578033176822,
    "fromUserId":0,
    "id":"1212985176135188480",
    "toUserId":7,
    "type":7
  },
  "message":"ok",
  "type":1002
}
```