package com.blanke.wechatRecordXposed.utils

import com.blanke.wechatRecordXposed.hook.Objects
import com.blanke.wechatRecordXposed.model.Setting
import com.google.gson.Gson
import java.io.*

object JsonHelper{
    fun getSetting(): Setting {
        val stringBuilder = StringBuilder()
        var fileInputStream:FileInputStream? = null
        var bufferedReader:BufferedReader? = null
        try {
            var file = File(Objects.SettingFilePath)
            if(!file.exists())
            {
                var setting = Setting(Objects.MP3DefaultOutputDir, 1)
                setSetting(setting)
                return setting
            }
            fileInputStream = FileInputStream(file)
            bufferedReader = BufferedReader(fileInputStream.reader())
            var line: String? = ""
            while (bufferedReader.readLine().also({ line = it }) != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        finally {
            bufferedReader?.close()
            fileInputStream?.close()
        }
        try {
            return Gson().fromJson(stringBuilder.toString(), Setting::class.java)
        }catch (e: Exception){
            return Setting(Objects.MP3DefaultOutputDir, 1)
        }
    }

    fun setSetting(setting: Setting?) {
        var outStream: FileOutputStream? = null
        try {
            var file = File(Objects.SettingFilePath)
            var parentDir = file.parentFile
            if(!parentDir.exists()){
                parentDir.mkdirs()
            }
            outStream = FileOutputStream(file)
            if(setting == null){
                return
            }
            var json = Gson().toJson(setting)
            outStream.write(json.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }finally {
            outStream?.close()
        }
    }
}