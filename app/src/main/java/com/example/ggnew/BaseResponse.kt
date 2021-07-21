package com.example.ggnew

import com.google.gson.annotations.SerializedName
import kotlin.properties.Delegates

class BaseResponse<T> {
    private var code by Delegates.notNull<Int>()
    private lateinit var msg:String

    @SerializedName("newslist")
    var data: T? = null

}