package com.example.ggnew

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.properties.Delegates

class NewAdapter(context: Context, resourceId: Int, data: List<New>) :
    ArrayAdapter<New>(context, resourceId, data) {
    private lateinit var mNewsData: List<New>
    private lateinit var mContext: Context
    private var resourceId by Delegates.notNull<Int>()

    init {
        this.mContext = context
        this.mNewsData = data
        this.resourceId = resourceId
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val news: New? = getItem(position)
        var view: View
        var vh: ViewHolder = ViewHolder()
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            vh.tvTitle = view.findViewById(R.id.tv_title);
            vh.tvSource = view.findViewById(R.id.tv_subtitle);
            vh.ivImageView = view.findViewById(R.id.iv_image);
            vh.ivDelete = view.findViewById(R.id.iv_delete);
            vh.tvPublishTime = view.findViewById(R.id.tv_publish_time);
            view.tag = ViewHolder();
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        vh.tvTitle?.setText(news?.mTitle);
        //vh.tvSource.setText(news?.mSource);
        vh.ivDelete?.setTag(position);
        vh.tvPublishTime?.setText(news?.mPublishTime);

        //ViewHolder.ivImageView
//        Glide.with(mContext).load(news?.mPicUrl).into(vh.ivImageView)
        vh.ivImageView?.let { Glide.with(mContext).load(news?.mPicUrl).into(it) }

        return view
    }

    inner class ViewHolder {
        var tvTitle: TextView? = null
        var tvSource: TextView? = null
        var ivImageView: ImageView? = null
        var tvPublishTime: TextView? = null
        var ivDelete: ImageView? = null
    }
}