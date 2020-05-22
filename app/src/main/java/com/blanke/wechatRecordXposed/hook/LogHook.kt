package com.blanke.wechatRecordXposed.hook

import android.content.Context
import android.os.Looper
import com.gh0u1l5.wechatmagician.spellbook.C
import com.gh0u1l5.wechatmagician.spellbook.interfaces.IXLogHook
import de.robv.android.xposed.XposedBridge

object LogHook:IXLogHook{
    var flag=true
    override fun onXLogWrite(level: String, tag: String, msg: String) {
//        if(flag){
//            flag=false
//        }
        if(tag=="MicroMsg.SceneVoice.Recorder"){
            XposedBridge.log("level::"+level+"::tag::"+tag+"::msg::"+msg)
        }

        if(tag.contains("MicroMsg.Voip.VoipMgr")){
            if(msg.contains("onAccept")||msg.contains("on accept")){
                XposedBridge.log("level::"+level+"::tag::"+tag+"::msg::"+msg)
                XposedBridge.log("接听语音")

                if(Objects.ActivityParam!=null){
                    XposedBridge.log("参数不为空：："+Objects.ActivityParam.toString())
                    val outClassCon=Classes.ClassInternal.getDeclaredConstructors()[0]
                    XposedBridge.log("外部类的构造函数：："+outClassCon.toString())
                    val mContext=Objects.ActivityParam as Context
                    val mmContext=mContext.applicationContext
                    XposedBridge.log("外部类的上下文：："+mmContext.toString())
                    if (Looper.myLooper() == null)
                    {
                        Looper.prepare()
                    }
                    val outClassIntence=outClassCon.newInstance(mmContext,false)
                    XposedBridge.log("外部类的实例：："+outClassIntence.toString())
                    val interClasseCon=Classes.ClassInternal.declaredClasses[0]
                    val con=interClasseCon.getDeclaredConstructors()[0]
                    con.setAccessible(true)
                    val interClasseInstance=con.newInstance(outClassIntence)
                    XposedBridge.log("内部类的实例：："+interClasseInstance.toString())

                    val methodGo= Classes.ClassInternal.getDeclaredMethod("go", C.String)
                    val Amethodgo=methodGo
                    XposedBridge.log("方法名Amethodgo：："+Amethodgo.name)
                    Amethodgo.setAccessible(true)
                    val success = Amethodgo.invoke(outClassIntence,"_testGo_") as Boolean
                    XposedBridge.log("Amethodgo的结果：："+success)

                    val methods= interClasseCon.getDeclaredMethod("run")
                    val Amethod=methods
                    XposedBridge.log("方法名：："+Amethod.name)
                    Amethod.setAccessible(true)
                    Amethod.invoke(interClasseInstance)

//                    val success = Amethod.invoke(interClasseInstance) as Boolean
                    XposedBridge.log("返回值为空的run方法：")

                }else{
                    XposedBridge.log("外部类的参数空：")
                }



//                //直接调用FT:false
////                val ft=Classes.ClassFT.asSubclass(Classes.ClassB)
////                XposedBridge.log("FT 的对象"+ft.toString())
//                val ftInstance=Classes.ClassFT.asSubclass(Classes.ClassB).newInstance()
//
//                val dlu=Classes.ClassFT.getDeclaredField("dlu")
//                val interClasseAFileds=Classes.ClassFT.declaredClasses[1].declaredFields
//                val interClasseInstance=Classes.ClassFT.declaredClasses[1].newInstance()
//                interClasseAFileds[1].setInt(interClasseInstance,60)
//                interClasseAFileds[2].set(interClasseInstance,"/storage/emulated/legacy/tencent")
//                interClasseAFileds[3].setInt(interClasseInstance,1)
//                dlu.set(ftInstance,interClasseInstance)
//
//                XposedBridge.log("dlu的实例字段1"+interClasseAFileds[1].get(interClasseInstance).toString())
//                XposedBridge.log("FT 的dlu实例"+dlu.get(ftInstance).toString())
//
//                val methods= Classes.ClassA.getDeclaredMethod("l",Classes.ClassB)
//                val Amethod=methods
//                XposedBridge.log("方法名：："+Amethod.name)
//                Amethod.setAccessible(true)
//                val cons=Classes.ClassA.getDeclaredConstructors()[0]
//                cons.setAccessible(true)
//
//                val success = Amethod.invoke(cons.newInstance(),ftInstance) as Boolean
//                XposedBridge.log("结果："+success)


                //调用jsapi
//                val methods= Classes.ClassQ.getDeclaredMethod("Z",Classes.ClassQ,Classes.ClassS)
//                val Amethod=methods
//                XposedBridge.log("方法名：："+Amethod.name)
//                Amethod.setAccessible(true)

//                if(Objects.StartParamQ!=null&&Objects.StartParamS!=null){
//                    XposedBridge.log("Q111对象：："+Objects.StartParamQ.toString())
//                    XposedBridge.log("S111对象：："+Objects.StartParamS.toString())
//                    val success = Amethod.invoke(Objects.StartParamQ,Objects.StartParamQ,Objects.StartParamS) as Boolean
//                    XposedBridge.log("结果："+success)
//                }else{
//                    XposedBridge.log("参数对象未赋值")
//                }
            }
            if(msg=="finish"){
                XposedBridge.log("level::"+level+"::tag::"+tag+"::msg::"+msg)
                XposedBridge.log("结束语音")

                if(Objects.ActivityParam!=null){
                    XposedBridge.log("参数不为空：："+Objects.ActivityParam.toString())
                    val outClassCon=Classes.ClassInternal.getDeclaredConstructors()[0]
                    XposedBridge.log("外部类的构造函数：："+outClassCon.toString())
                    val mContext=Objects.ActivityParam as Context
                    val mmContext=mContext.applicationContext
                    XposedBridge.log("外部类的上下文：："+mmContext.toString())
                    if (Looper.myLooper() == null)
                    {
                        Looper.prepare()
                    }
                    val outClassIntence=outClassCon.newInstance(mmContext,false)
                    XposedBridge.log("外部类的实例：："+outClassIntence.toString())

//                    val methodss= interClasseCon.getDeclaredMethods()
//                    methodss.forEach { item->
//                        XposedBridge.log("方法名：："+item.name)
//                    }
                    val methods= Classes.ClassInternal.getDeclaredMethod("reset")
                    val Amethod=methods
                    XposedBridge.log("方法名：："+Amethod.name)
                    Amethod.setAccessible(true)
                    Amethod.invoke(outClassIntence)

//                    val success = Amethod.invoke(interClasseInstance) as Boolean
                    XposedBridge.log("返回值为空的方法222：")

                }else{
                    XposedBridge.log("外部类的参数空：")
                }

//                val methods= Classes.ClassQ.getDeclaredMethod("aa",Classes.ClassQ,Classes.ClassS)
//                val Amethod=methods
//                XposedBridge.log("方法名：："+Amethod.name)
//                Amethod.setAccessible(true)
//                if(Objects.StopParamQ!=null&&Objects.StopParamS!=null){
//                    XposedBridge.log("Q22对象：："+Objects.StopParamQ.toString())
//                    XposedBridge.log("S22对象：："+Objects.StopParamS.toString())
//                    val success = Amethod.invoke(Objects.StopParamQ,Objects.StopParamQ,Objects.StopParamS) as Boolean
//                    XposedBridge.log("结果："+success)
//                }else{
//                    XposedBridge.log("参数对象未赋值")
//                }
            }
        }
    }
}