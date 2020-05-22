package com.blanke.wechatRecordXposed.hook

import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxClasses
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxLazy
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxLoader
import com.gh0u1l5.wechatmagician.spellbook.WechatGlobal.wxPackageName
import com.gh0u1l5.wechatmagician.spellbook.util.ReflectionUtil.findClassesFromPackage
import de.robv.android.xposed.XposedBridge

object Classes {
    val ClassQ: Class<*> by wxLazy("ChattingFooterEventImpl") {
      var obj= findClassesFromPackage(wxLoader!!, wxClasses!!, "$wxPackageName.plugin.webview.ui.tools.jsapi")
                .filterByMethod( C.Boolean,"SM",C.String)
                .filterByMethod( C.Boolean,"jR",C.String,C.String)
                .firstOrNull()
        XposedBridge.log("当前Classes111"+obj.toString())
        obj
    }
    val ClassS: Class<*> by wxLazy("ClassS") {
        var obj=findClassesFromPackage(wxLoader!!, wxClasses!!, "$wxPackageName.plugin.webview.ui.tools.jsapi")
                .filterByMethod( C.Map,"eit")
                .firstOrNull()
        XposedBridge.log("当前Classes222"+obj.toString())
        obj
    }
    val ClassFT: Class<*> by wxLazy("ClassFT") {
        var obj=findClassesFromPackage(wxLoader!!, wxClasses!!, "$wxPackageName.g.a")
                .filterByField( "dlu","$wxPackageName.g.a.ft.a")
                .firstOrNull()
        XposedBridge.log("当前Classes3333"+obj.toString())
        obj
    }
    val ClassA: Class<*> by wxLazy("ClassA") {
        var obj=findClassesFromPackage(wxLoader!!, wxClasses!!, "$wxPackageName.sdk.b")
                .filterByMethod( C.Boolean,"l",ClassB)
                .firstOrNull()
        XposedBridge.log("当前Classes444"+obj.toString())
        obj
    }
    val ClassB: Class<*> by wxLazy("ClassB") {
        var obj=findClassesFromPackage(wxLoader!!, wxClasses!!, "$wxPackageName.sdk.b")
                .filterByMethod( C.Boolean,"evJ")
                .firstOrNull()
        XposedBridge.log("当前Classes555"+obj.toString())
        obj
    }

    val ClassInternal: Class<*> by wxLazy("ClassB") {
        var obj=findClassesFromPackage(wxLoader!!, wxClasses!!, "$wxPackageName.audio.b")
                .filterByMethod( C.Boolean,"go",C.String)
                .firstOrNull()
        XposedBridge.log("当前Classes666"+obj.toString())
        obj
    }
}
