# WechatRecordXposed
 

## 背景
###### **基于微信7.0.13版本制作，其他版本微信需要复制相应的方法名到HOOK方法中**
###### **微信语音/视频开会**，~~一遍听不清楚~~  ？
######	        和客户微信语音沟通~~过后又忘记重点~~ ？
######	        想**保留**和**重要的**人的**语音音频**？
######              **懒得**用另外的设备录音？

## 解决方案

 既然微信没有保存语音聊天的功能，自己加一个不就好了吗？！

 ~~本文结束！~~ 
 ![Alt]![在这里插入图片描述](https://img-blog.csdnimg.cn/20200522150808744.png)
## 功能实现

1、 依赖Xposed框架（非root环境），给微信添加自定义的功能；
2、 微信语音文件（.amr格式）生成到自定义目录
	   	 

> *（ 默认目录：SD卡/tencent/micromsg/用户/voice2）*

3、 提供将.amr格式文件转换成.mp3的功能；


## 使用流程


 1. 安装一个Xposed框架（推荐[Xpatch](https://github.com/Zdelta/Xpatch)）
 2. 将微信和此模块添加到框架里
 3. 语音/视频通话时，建议**开启免提**，使对方的声音更清楚
 4. 正常使用微信即可生成文件，平时无感知

## 预览

 1. 点击左.amr文件可在右侧生成.mp3格式文件
 2. 点击mp3文件点击可直接播放
 3. 可在右上角设置mp3输出文件路径

![录音文件](https://img-blog.csdnimg.cn/20200522142110295.png)![播放语音](https://img-blog.csdnimg.cn/20200522142153334.png)


## 思路

```javascript
//发起或接听语音视频通话时，调用微信音频方法

object LogHook:IXLogHook{
    //直接从日志入手
    override fun onXLogWrite(level: String, tag: String, msg: String) {
         if(tag.contains("MicroMsg.Voip.VoipMgr")){
            if(msg.contains("onAccept")||msg.contains("on accept")){//接听
                    if(Objects.ActivityParam!=null){
                    val outClassCon=Classes.ClassInternal.getDeclaredConstructors()[0]
                    val mContext=Objects.ActivityParam as Context
                    val mmContext=mContext.applicationContext
                    val outClassIntence=outClassCon.newInstance(mmContext,false)
                    val interClasseCon=Classes.ClassInternal.declaredClasses[0]
                    val con=interClasseCon.getDeclaredConstructors()[0]
                    con.setAccessible(true)
                    val interClasseInstance=con.newInstance(outClassIntence)
                    val methodGo= Classes.ClassInternal.getDeclaredMethod("go", C.String)
                    val Amethodgo=methodGo
                    Amethodgo.setAccessible(true)
                    val success = Amethodgo.invoke(outClassIntence,"_pathGo_") as Boolean
                    val methods= interClasseCon.getDeclaredMethod("run")
                    val Amethod=methods
                    Amethod.setAccessible(true)
                    Amethod.invoke(interClasseInstance)
                }
            if(msg=="finish"){//挂断
                if(Objects.ActivityParam!=null){
                    val outClassCon=Classes.ClassInternal.getDeclaredConstructors()[0]
                    val mContext=Objects.ActivityParam as Context
                    val mmContext=mContext.applicationContext
                    val outClassIntence=outClassCon.newInstance(mmContext,false)
                    val methods= Classes.ClassInternal.getDeclaredMethod("reset")
                    val Amethod=methods
                    Amethod.setAccessible(true)
                    Amethod.invoke(outClassIntence)
                }
```

## 待实现的功能

1. 使用微信备注和联系人昵称替换随机文件名；
2. 支持录音时长设置
3. 音频文件生成延迟结束的问题
4. 播放页优化

~~为什么会有bug呢……第一次做android，第一次用kotlin，两件快乐事情重合在一起。而这两份快乐，又给我带来更多的快乐。得到的，本该是一次通过、完美运行……但是，为什么，会变成这样呢……~~ 
![啪](https://img-blog.csdnimg.cn/2020052215020963.png)
## 已测试设备
==备注==：手机系统必须支持多个应用同时使用音频接口（如微信和微信小程序可以同时录音）
测试设备     | 结果
-------- | -----
小米miui11、安卓10  | 支持
华为Emui9、安卓9  | 不支持
google pix、安卓10  | 支持
oppo、安卓10  | 不支持

#### 参考/引用的项目
[weixin_silk_2_mp3](https://github.com/zhangchenghai2015/weixin_silk_2_mp3)
[WechatBotXposed](https://github.com/Blankeer/WechatBotXposed)
