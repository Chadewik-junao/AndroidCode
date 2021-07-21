package com.example.ggnew

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class New {
    @Expose(serialize = false, deserialize = false)
    var mId: Int = 0

    @SerializedName("title")
    var mTitle: String? = null

    @SerializedName("description")
    var mSource: String? = null

    @SerializedName("picUrl")
    var mPicUrl: String? = null;

    @SerializedName("url")
    var mContentUrl: String? = null;

    @SerializedName("ctime")
    var mPublishTime: String? = null;
}