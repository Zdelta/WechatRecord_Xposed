package com.blanke.wechatRecordXposed.hook

import android.os.Looper
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.interfaces.IXLogHook
import de.robv.android.xposed.XposedBridge

object LogHook:IXLogHook{

    override fun onXLogWrite(level: String, tag: String, msg: String) {
        if(tag=="MicroMsg.SceneVoice.Recorder"){
            XposedBridge.log("level::"+level+"::tag::"+tag+"::msg::"+msg)
        }

        if(tag.contains("MicroMsg.Voip.VoipMgr")){
            if(msg.contains("onAccept")||msg.contains("on accept")){
                XposedBridge.log("接听语音")

                if(Objects.ActivityParam!=null){
                    val outClassIntence=SingletonClassInternal.get()

                    val dcp=outClassIntence.javaClass.getDeclaredField("dcp")
                    dcp.setAccessible(true)
                    dcp.setInt(outClassIntence,2)

                    val interClasseCon=outClassIntence.javaClass.declaredClasses[0]
                    val con=interClasseCon.getDeclaredConstructors()[0]
                    con.setAccessible(true)
                    if (Looper.myLooper() == null)
                    {
                        Looper.prepare()
                    }
                    val interClasseInstance=con.newInstance(outClassIntence)

                    val methodGo= Classes.ClassInternal.getDeclaredMethod("go", C.String)
                    val Amethodgo=methodGo
                    Amethodgo.setAccessible(true)
                    val success = Amethodgo.invoke(outClassIntence,"_testGo_") as Boolean
                    XposedBridge.log("go方法结果：："+success)

                    val methods= interClasseCon.getDeclaredMethod("run")
                    val Amethod=methods
                    Amethod.setAccessible(true)
                    Amethod.invoke(interClasseInstance)
                    XposedBridge.log("run方法：")

                }else{
                    XposedBridge.log("外部类的参数空：")
                }

            }
            if(msg=="finish"){
                XposedBridge.log("结束语音")

                if(Objects.ActivityParam!=null){
                    val outClassIntence=SingletonClassInternal.get()
                    XposedBridge.log("外部类的实例：："+outClassIntence.toString())

                    val dcp=outClassIntence.javaClass.getDeclaredField("dcp")
                    dcp.setAccessible(true)
                    dcp.setInt(outClassIntence,2)

                    val methods= Classes.ClassInternal.getDeclaredMethod("Fo")
                    val Amethod=methods
                    Amethod.setAccessible(true)
                    val success = Amethod.invoke(outClassIntence) as Boolean
                    XposedBridge.log("停止方法："+success)
                }else{
                    XposedBridge.log("外部类的参数空：")
                }
            }
        }
    }
}