package com.blanke.wechatRecordXposed.hook

import android.util.Log
import com.blanke.wechatRecordXposed.utils.JsonHelper
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal
import com.gh0u1l5.wechatmagician.spellbook.base.Hooker
import com.gh0u1l5.wechatmagician.spellbook.base.HookerProvider
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedHelpers.findAndHookMethod

object SendMsgHooker : HookerProvider {

    override fun provideStaticHookers(): List<Hooker>? {
        return listOf(methodAudioHook)
    }

    private val methodAudioHook = Hooker {
        val clz = XposedHelpers.findClass("com.tencent.mm.audio.b.b", WechatGlobal.wxLoader)
        XposedBridge.log("clzA::::当前类"+clz.toString())
        findAndHookMethod(clz, "setMaxDuration", C.Int, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                XposedBridge.log("hook=====audio======方法")
                if(param!=null){
                    val time=param.args[0] as Int
                    if (time==70000){
                        //param.args[0]= setting.minute.toInt() * 60 * 1000
                        param.args[0] = 130000
                        Log.i("Test", param.args[0].toString())
                        XposedBridge.log("-------------------修改后的录音时长-------------------：" + param.args[0].toString())
                    }
                }else{
                    XposedBridge.log("hook 方法空参数" + param?.thisObject)
                }
            }
        })
    }
}