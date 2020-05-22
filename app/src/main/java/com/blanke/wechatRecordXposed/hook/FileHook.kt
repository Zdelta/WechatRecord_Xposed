package com.blanke.wechatRecordXposed.hook

import com.gh0u1l5.wechatmagician.spellbook.interfaces.IFileSystemHook
import de.robv.android.xposed.XposedBridge
import java.io.File

object FileHook: IFileSystemHook{
    override fun onFileReading(file: File) {
        if(file.name.contains(".amr")){
            XposedBridge.log("输入流方法"+file.absoluteFile)
            if(file.name.toLowerCase().contains("jsapi")){
                XposedBridge.log("文件名：："+file.name)
            }
        }

    }

    override fun onFileWriting(file: File, append: Boolean) {
        if(file.name.contains(".amr")){
            XposedBridge.log("输出流方法"+file.name)
        }

    }


}