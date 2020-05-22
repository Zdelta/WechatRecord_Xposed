package com.blanke.wechatRecordXposed.hook

import android.content.Context
import android.os.Looper

object Objects {

    var ActivityParam:Any?=null

    var MP3DefaultOutputDir:String = "/storage/emulated/0/tencent/MicroMsg/convertToMP3"

    var SettingFilePath:String = "/storage/emulated/0/WechatRecord/setting.json"

    var DEFAULT_MINUTE:Int = 1

}

class SingletonClassInternal private constructor(){
    companion object{
        private var instance: Any? = null
            get(){
                if(field == null){
                    val outClassCon=Classes.ClassInternal.getDeclaredConstructors()[0]
                    val mContext=Objects.ActivityParam as Context
                    val mmContext=mContext.applicationContext
                    if (Looper.myLooper() == null)
                    {
                        Looper.prepare()
                    }
                    field=outClassCon.newInstance(mmContext,false)
                }
                return field
            }
        @Synchronized
        fun get():Any{
            return instance!!
        }
    }
}


