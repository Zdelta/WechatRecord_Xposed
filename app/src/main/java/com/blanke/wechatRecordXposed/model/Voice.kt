package com.blanke.wechatRecordXposed.model

class Voice{
    var name:String =""
    var filePath:String = ""
    var updateTime:Long = 0
    var size:Long = 0

    constructor(name:String, filePath:String, updateTime:Long, size:Long){
        this.filePath = filePath
        this.name = name
        this.updateTime = updateTime
        this.size = size
    }
}