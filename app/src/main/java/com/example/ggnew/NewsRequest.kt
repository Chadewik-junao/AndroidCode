package com.example.ggnew

import kotlin.properties.Delegates

class NewsRequest {
    var num by Delegates.notNull<Int>()
    var col by Delegates.notNull<Int>()
    var page = -1
    var rand by Delegates.notNull<Int>()
    lateinit var keyword: String

    override fun toString(): String {
        var retValue: String? = null
        retValue = "?" + "&key=" + Constant.API_KEY + "&num=" + num + "&col=" + col
        if (page != -1) {
            retValue += "&page=" + page
        }
        return retValue
    }
}