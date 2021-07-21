package com.example.ggmusic03

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.lang.UnsupportedOperationException

class MusicService : Service() {
    var mMediaPlayer:MediaPlayer?=null
    private val ONGOING_NOTIFICATION_ID=1001
    private val CHANNL_ID="Music channel"
    var mNotificationManager:NotificationManager?=null

    override fun onDestroy(){
        mMediaPlayer?.stop()
        mMediaPlayer?.release()
        mMediaPlayer=null
        super.onDestroy()
    }

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer=MediaPlayer()
    }

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data=intent?.getStringExtra("com.example.ggmusic03.DATA_URI")
        val dataUri=Uri.parse(data)
        val title=intent?.getStringExtra("MainActivity.TITLE")
        val artist=intent?.getStringExtra("MainActivity.ARTIST")
        if (mMediaPlayer!=null){
            try {
                mMediaPlayer!!.reset()
                mMediaPlayer!!.setDataSource(applicationContext,dataUri)
                mMediaPlayer!!.prepare()
                mMediaPlayer!!.start()
            }catch (ex:IOException){
                ex.printStackTrace()
            }
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            mNotificationManager= getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
            val channel: NotificationChannel =NotificationChannel(CHANNL_ID,"Music Channel",NotificationManager.IMPORTANCE_HIGH)

            if (mNotificationManager!=null){
                mNotificationManager!!.createNotificationChannel(channel)
            }
        }

        val notificationIntent:Intent=Intent(applicationContext,MainActivity::class.java)
        val pendingIntent:PendingIntent= PendingIntent.getActivities(applicationContext,0, arrayOf(notificationIntent),0)
        val builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,CHANNL_ID)
        val notification:Notification=builder.setContentTitle(title).setContentText(artist).setSmallIcon(R.drawable.ic_launcher_foreground).setContentIntent(pendingIntent).build()
        startForeground(ONGOING_NOTIFICATION_ID,notification)//需要前台服务权限

        return super.onStartCommand(intent, flags, startId)
    }
}

//private val mBinder: IBinder = MusicServiceBinder()
//
//
//override fun onBind(intent: Intent?): IBinder? {
//    return mBinder
//}
//
////inner非静态内部类
////this@MusicService返回当前父类对象
//inner class MusicServiceBinder() : Binder() {
//    fun getService(): MusicService {
//        return this@MusicService
//    }
//}
//
//public fun pause() {
//    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//        mMediaPlayer.pause()
//    }
//}
//
//public fun play() {
//    mMediaPlayer?.start()
//}
//
//public fun getDuration():Int {
//    var duration = 0
//    if (mMediaPlayer!=null) duration = mMediaPlayer.getDuration()
//    return duration
//}
//
//public fun getCurrentPosition():Int{
//    var position=0
//    position=mMediaPlay?.getCurrentPosition()
//    return position
//}
//
//public fun isPlaying():Boolean{
//    if (mMediaPlayer!=null){
//        return mMediaPlayer.isPlaying()
//    }
//    return falsed
//}