package com.example.code09

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


class NewAdapter(activity: Activity, val resourceId:Int, data:List<New>): ArrayAdapter<New>(activity,resourceId,data) {
    override fun getView(position:Int, convertView: View?, parent: ViewGroup): View {
        val view= LayoutInflater.from(context).inflate(resourceId,parent,false)
        val tvTitle: TextView =view.findViewById(R.id.tv_title)
        val tvAuthor : TextView =view.findViewById(R.id.tv_subtitle)
        val ivImage: ImageView =view.findViewById(R.id.iv_image)
        val new: New? =getItem(position)
        if(new!=null){
            tvTitle.setText(new.mTile)
            tvAuthor.setText(new.mAuther)
//            ivImage.setImageResource(new.mImageId)
            ivImage.setImageBitmap(new.mImageId)
        }
        return view
    }
}