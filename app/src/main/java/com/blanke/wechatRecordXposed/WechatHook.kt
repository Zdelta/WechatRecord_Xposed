package com.blanke.wechatRecordXposed

import com.blanke.wechatRecordXposed.hook.ActivityHook
import com.blanke.wechatRecordXposed.hook.LogHook
import com.blanke.wechatRecordXposed.hook.SendMsgHooker
import com.gh0u1l5.wechatmagician.spellbook.SpellBook
import com.gh0u1l5.wechatmagician.spellbook.util.BasicUtil
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WechatHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        BasicUtil.tryVerbosely {
            if (SpellBook.isImportantWechatProcess(lpparam)) {
                XposedBridge.log("Hello Wechat!")
                SpellBook.startup(lpparam,
                        listOf(SendMsgHooker, ActivityHook, LogHook)
                )
            }
        }
    }
}