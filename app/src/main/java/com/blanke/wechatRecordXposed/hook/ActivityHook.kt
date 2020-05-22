package com.blanke.wechatRecordXposed.hook

import android.app.Activity
import com.gh0u1l5.wechatmagician.spellbook.interfaces.IActivityHook
import de.robv.android.xposed.XposedBridge

object ActivityHook :IActivityHook{
    override fun onActivityResuming(activity: Activity) {
        if(Objects.ActivityParam==null){
            if(activity.toString().contains("voip.ui.VideoActivity")){
                Objects.ActivityParam=activity
                XposedBridge.log("当前activity：："+activity.toString())
            }
        }
    }
}