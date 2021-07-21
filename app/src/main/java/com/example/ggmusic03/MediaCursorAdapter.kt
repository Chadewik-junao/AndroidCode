package com.example.ggmusic03

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView


class MediaCursorAdapter(val context: Context) : CursorAdapter(context, null, 0) {
    private lateinit var mContext: Context
    private lateinit var mLayoutInflater: LayoutInflater

    //主构造函数初始化逻辑
    init {
        mContext = context
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val itemView=mLayoutInflater.inflate(R.layout.list_item,parent,false)
        if (itemView!=null){
            var vh:ViewHolder =ViewHolder()
            vh.tvTitle = itemView.findViewById(R.id.tv_title);
            vh.tvArtist = itemView.findViewById(R.id.tv_artist)
            vh.tvOrder = itemView.findViewById(R.id.tv_order)
            vh.divider = itemView.findViewById(R.id.divider)
            itemView.tag = vh
        }
        return itemView//空
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        //Log.d("view Debug",view.tag.toString())
        val vh:ViewHolder?= view.tag as ViewHolder?
        var titleIndex= cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
        var artistIndex= cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

        var title= titleIndex.let { cursor.getString(it) }//非空判断
        var artist= artistIndex.let { cursor.getString(it) }
        var position=cursor.position

        if (vh!=null){
            vh.tvTitle.setText(title)
            vh.tvArtist.setText(artist)
            if (position != null) {
                vh.tvOrder.setText(Integer.toString(position+1))
            }
        }
    }

    public class ViewHolder{
        lateinit var tvTitle: TextView
        lateinit var tvArtist: TextView
        lateinit var tvOrder: TextView
        lateinit var divider: View
    }
}