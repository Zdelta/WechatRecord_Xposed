package com.blanke.wechatRecordXposed.hook

import android.app.Application
import android.content.Context

object Objects {
//    val ChattingFooterEventImpl= Classes.ChattingFooterEventImpl.getDeclaredConstructors()[0].newInstance(1)

    var StartParamQ: Any? =null
    var StartParamS:Any?=null

    var StopParamQ: Any? =null
    var StopParamS:Any?=null

    var ActivityParam:Any?=null
    var one:Any?=null
    var two:Any?=null
//    val ClassS= Classes.ClassS.getDeclaredConstructors()[0].newInstance()

}
//
//class SingletonDemo private constructor(){
//    companion object{
//        private var instance: Any? = null
//            get(){
//                if(field == null){
//                    val cons=Classes.ClassQ.getDeclaredConstructors()[0]
//                    cons.setAccessible(true)
//                    field =cons .newInstance(0)
//                }
//                return field
//            }
//        @Synchronized
//        fun get():Any{
//            return instance!!
//        }
//    }
//}


