package com.nishanth.retrofitdemo

import com.google.gson.annotations.SerializedName

data class Post (val userId:String?,val title:String?, @SerializedName("body") val text:String?,val id:Int?=null){
}