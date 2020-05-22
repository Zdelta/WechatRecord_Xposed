package com.blanke.wechatRecordXposed.hook

import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxClasses
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxLazy
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxLoader
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxPackageName
import com.gh0u1l5.wechatmagician.spellbook.util.ReflectionUtil.findClassesFromPackage
import de.robv.android.xposed.XposedBridge

object Classes {
    val ClassInternal: Class<*> by wxLazy("ClassB") {
        var obj=findClassesFromPackage(wxLoader!!, wxClasses!!, "$wxPackageName.audio.b")
                .filterByMethod( C.Boolean,"go",C.String)
                .firstOrNull()
        XposedBridge.log("当前Classes666"+obj.toString())
        obj
    }
}
