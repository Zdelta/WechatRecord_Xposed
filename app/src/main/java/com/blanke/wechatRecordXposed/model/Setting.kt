package com.blanke.wechatRecordXposed.model

class Setting{

    var outputPath:String = ""
    var minute:String = "0"

    constructor(path:String, minute:Int){
        this.outputPath = path
        this.minute = minute.toString()
    }

    override fun toString(): String {
        return "outputPath:"+outputPath+",minute:"+minute
    }
}