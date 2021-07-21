package com.example.ggmusic04

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ThemedSpinnerAdapter
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_media_toolbar.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val DATA_URI = "com.example.ggmusic04.DATA_URI"
    private val TITLE = "com.example.ggmusic04.TITLE"
    private val ARTIST = "com.example.ggmusic04.ARTIST"

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

//    private var ivPlay: ImageView? =null
//    private var tvBottomTitle: TextView? =null
//    private var ivAlbumThumbnail: ImageView?=null

    private  var mService: MusicService? = null
    private var mBound: Boolean = false
    private val mConn = object : ServiceConnection {
        //匿名类实现接口
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binger: MusicService.MusicServiceBinder = service as MusicService.MusicServiceBinder
            mService = binger.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            mBound = false
        }
    }

    private var mPlayStatus=true


    private val UPDATE_PROGRESS=1
//    private var pbProgress:ProgressBar?=null
    private val ACTION_MUSIC_START="com.example.ggmusic04.ACTION_MUSIC_START"
    private val ACTION_MUSIC_STOP="com.example.ggmusic04.ACTION_MUSIC_STOP"
    private var musicReceiver:MusicReceiver?=null



    private var mHandler=object:Handler(Looper.getMainLooper()){ override fun handleMessage(msg:Message){
            when(msg.what){
                UPDATE_PROGRESS ->{
                    val position=msg.arg1
                    progress.setProgress(position)
                }
                else -> {}
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.M)
    //高于M棉花糖版本的安卓程序将会执行这段代码，否则将不工作
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化
        mContentResolver = contentResolver
        mCursorAdapter = MediaCursorAdapter(this)


        musicReceiver=MusicReceiver()
        val intentFilter=IntentFilter()
        intentFilter.addAction(ACTION_MUSIC_START)
        intentFilter.addAction(ACTION_MUSIC_STOP)
        registerReceiver(musicReceiver,intentFilter)

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

    override fun onDestroy() {
        unregisterReceiver(musicReceiver)
        super.onDestroy()
    }

    private val itemClickListener: AdapterView.OnItemClickListener =
        AdapterView.OnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
            //添加监听器
            //获取点击内容并播放
            val cursor = mCursorAdapter?.cursor
            if (cursor != null && cursor.moveToPosition(i)) {
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                val dataIndex = cursor.getColumnIndex("_data")

                val title = cursor.getString(titleIndex)
                val artist = cursor.getString(artistIndex)
                val albumId = cursor.getLong(albumIdIndex)
                val data = cursor.getString(dataIndex)

                val dataUri = Uri.parse(data)

                val serviceIntent = Intent(this, MusicService::class.java)
                serviceIntent.putExtra(this.DATA_URI, data)
                serviceIntent.putExtra(this.TITLE, title)
                serviceIntent.putExtra(this.ARTIST, artist)

                startService(serviceIntent)

                //直接进行播放
//            if (mMediaPlayer!=null){
//                try {
//                    mMediaPlayer!!.reset()
//                    mMediaPlayer!!.setDataSource(this,dataUri)
//                    mMediaPlayer!!.prepare()
//                    mMediaPlayer!!.start()
//                }catch (ex: IOException){
//                    ex.printStackTrace()
//                }
//            }
                navigation.visibility = View.VISIBLE

                //更新控制栏信息
                tv_bottom_title?.setText(title)
                tv_bottom_artist?.setText(artist)

                val albumUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                )

                val albumCursor = mContentResolver?.query(albumUri, null, null, null, null)

                if (albumCursor != null && albumCursor.count > 0) {
                    albumCursor.moveToFirst()
                    val albumArtIndex = albumCursor.getColumnIndex("album_art")
                    val albumArt = albumCursor.getString(albumArtIndex)
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
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) initPlaylist()
        }
    }

    override fun onStart() {
        super.onStart()
//        if (mMediaPlayer == null) {
//            mMediaPlayer = MediaPlayer()
//        }
        var intent=Intent(this,MusicService::class.java)
        bindService(intent,mConn,Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
//        mMediaPlayer?.stop()
//        mMediaPlayer?.release()
//        if (mMediaPlayer != null) {
//            Log.d("TAG", "onStop invoked")
//            mMediaPlayer = null
//        }
        unbindService(mConn)
        mBound=false
        super.onStop()
    }

    override fun onClick(v: View?) {
        if (v?.id==R.id.iv_play){
            mPlayStatus=!mPlayStatus
            if (mPlayStatus==true){
                mService?.play()
                iv_play.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24)
            }else{
                mService?.pause()
                iv_play.setImageResource(R.drawable.ic_baseline_play_circle_outline_24)
            }
        }
    }

    //内部线程类，耦合度高不易维护，简化项目
    inner class MusicProgressRunnable:Runnable{
        override fun run() {
            var mThreadWorking=true
            while (mThreadWorking){
                try {
                    if (mService!=null){
                        val position= mService!!.getCurrentPosition()
                        var message=Message()
                        message.what=UPDATE_PROGRESS
                        message.arg1=position
                        mHandler.sendMessage(message)
                    }
                    mThreadWorking=mService!!.isPlaying()
                    Thread.sleep(20)
                }catch (ie:InterruptedException){
                    ie.printStackTrace()
                }
            }
        }
    }

    inner class MusicReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (mService!=null){
                progress.max=mService!!.getDuration()
                Thread(MusicProgressRunnable()).start()
            }
        }

    }

}