package com.blanke.wechatRecordXposed.hook

import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object SendMsgHooker : HookerProvider {

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(methodStartRecordHook, methodStopRecordHook)//,methodHParamHook)
    }

    private val methodStopRecordHook = Hooker {
        val clz = XposedHelpers.findClass("com.tencent.mm.plugin.webview.ui.tools.jsapi.q", WechatGlobal.wxLoader)
        XposedBridge.log("clzZ::::当前类"+clz.toString())
        findAndHookMethod(clz, "aa",Classes.ClassQ,Classes.ClassS, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                XposedBridge.log("hook=====A====== 构造方法")
                if(param!=null){
                    XposedBridge.log("hook 方法获取参数对象" + param.args.count())
                    XposedBridge.log("第一个对象" + param.args[0].toString())
                    Objects.StopParamQ= param.args[0]
                    XposedBridge.log("第二个对象" + param.args[1].toString())
                    Objects.StopParamS= param.args[1]
                }else{
                    XposedBridge.log("hook 方法空的对象" + param?.thisObject)
                }
            }
        })
    }

    private val methodStartRecordHook = Hooker {
        val clz = XposedHelpers.findClass("com.tencent.mm.plugin.webview.ui.tools.jsapi.q", WechatGlobal.wxLoader)
        XposedBridge.log("clzA::::当前类"+clz.toString())
        findAndHookMethod(clz, "Z",Classes.ClassQ,Classes.ClassS, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                XposedBridge.log("hook=====Z====== 构造方法")
                if(param!=null){
                    XposedBridge.log("hook 方法获取参数对象" + param.args.count())
                    XposedBridge.log("第一个对象" + param.args[0].toString())
                    Objects.StartParamQ= param.args[0]
                    XposedBridge.log("第二个对象" + param.args[1].toString())
                    Objects.StartParamS= param.args[1]
                }else{
                    XposedBridge.log("hook 方法空的对象" + param?.thisObject)
                }
            }
        })
    }
    private val methodHParamHook = Hooker {
        XposedBridge.hookAllConstructors(Classes.ClassInternal, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                XposedBridge.log("hook=====参数类的====== 构造方法")
                if(param!=null){
                    XposedBridge.log("hook 方法获取参数对象" + param.args.count())
                    XposedBridge.log("第一个对象" + param.args[0].toString())
                    Objects.one= param.args[0]
                    XposedBridge.log("第二个对象" + param.args[1].toString())
                    Objects.two= param.args[1]
                }else{
                    XposedBridge.log("hook 方法空的对象" + param?.thisObject)
                }
            }
        })
    }
}