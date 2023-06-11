package com.nishanth.firestoredemo

class Note{
    var title:String?=null
    var description:String?=null
    var priority:Int?=null
    fun Note(){

    }
    fun Note(nTitle:String?, nDescription:String?, nPriority:Int?){
        this.title=nTitle
        this.description=nDescription
        this.priority=nPriority
    }
}
