package com.example.ggmusic02

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_media_toolbar.*
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE: Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var mMediaPlayer: MediaPlayer? = null
    private var mContentResolver: ContentResolver? = null

    //    private lateinit var mPlaylist: ListView
    private var mCursorAdapter: MediaCursorAdapter? = null
    private var mCursor: Cursor? = null

    private val SELECTION = MediaStore.Audio.Media.IS_MUSIC + " = ? " + " AND " +
            MediaStore.Audio.Media.MIME_TYPE + " LIKE ? "
    private val SELECTION_ARGS: Array<String> = arrayOf(1.toString(), "audio/mpeg")

    private var ivPlay: ImageView? =null
    private var tvBottomTitle: TextView? =null
    private var ivAlbumThumbnail:ImageView?=null




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化
        mContentResolver = contentResolver
        mCursorAdapter = MediaCursorAdapter(this)

        //获取权限
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED/*获取权限，获得返回PERMISSION_GRANTED,否则返回.PERMISSION_DENIED*/
        ) {
            //再次申请权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
            ) {
            } else {
                requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE)
            }
        } else initPlaylist()//初始化播放器列表


        lv_playlist.adapter = mCursorAdapter

        LayoutInflater.from(this).inflate(R.layout.bottom_media_toolbar, navigation, true)
//        ivPlay=navigation.findViewById<ImageView>(R.id.iv_play)
//        tvBottomTitle=navigation.findViewById<TextView>(R.id.tv_bottom_title)
//        ivAlbumThumbnail=navigation.findViewById<ImageView>(R.id.iv_thumbnail)
        iv_play?.setOnClickListener(this);

        navigation.visibility = View.GONE

        lv_playlist.setOnItemClickListener(itemClickListener)
    }

    private val itemClickListener: AdapterView.OnItemClickListener =AdapterView.OnItemClickListener{ _: AdapterView<*>, _: View, i: Int, _: Long ->
        //添加监听器
        //获取点击内容并播放
        val cursor=mCursorAdapter?.cursor
        if(cursor!=null&&cursor.moveToPosition(i)){
            val titleIndex=cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistIndex=cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumIdIndex=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val dataIndex=cursor.getColumnIndex("_data")

            val title=cursor.getString(titleIndex)
            val artist=cursor.getString(artistIndex)
            val albumId=cursor.getLong(albumIdIndex)
            val data=cursor.getString(dataIndex)

            val dataUri= Uri.parse(data)

            //播放
            if (mMediaPlayer!=null){
                try {
                    mMediaPlayer!!.reset()
                    mMediaPlayer!!.setDataSource(this,dataUri)
                    mMediaPlayer!!.prepare()
                    mMediaPlayer!!.start()
                }catch (ex: IOException){
                    ex.printStackTrace()
                }
            }
            navigation.visibility=View.VISIBLE

            //更新控制栏信息
            tv_bottom_title?.text = title
            tv_bottom_artist?.text = artist

            val albumUri:Uri=ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,albumId)

            val albumCursor=mContentResolver?.query(albumUri,null,null,null,null)

            if (albumCursor!=null&&albumCursor.count>0){
                albumCursor.moveToFirst()
                val albumArtIndex=albumCursor.getColumnIndex("album_art")
                val albumArt=albumCursor.getString(albumArtIndex)
                Glide.with(this).load(albumArt).into(iv_thumbnail as ImageView)
                albumCursor.close()
            }
        }

    }


    private fun initPlaylist() {
        mCursor = mContentResolver?.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                SELECTION,
                SELECTION_ARGS,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
//        mCursor?.count
        mCursorAdapter?.swapCursor(mCursor)
        mCursorAdapter?.notifyDataSetChanged()

    }

    //重载函数
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {//泛型out表示只读，in表示只写
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) initPlaylist()
        }
    }

    override fun onStart() {
        super.onStart()
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
    }

    override fun onStop() {
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        if (mMediaPlayer != null) {
            Log.d("TAG", "onStop invoked")
            mMediaPlayer = null
        }
        super.onStop()
    }

    override fun onClick(v: View?) {

    }

}