package com.example.ggmusic

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE: Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private  var mContentResolver: ContentResolver?=null
//    private lateinit var mPlaylist: ListView
    private  var mCursorAdapter: MediaCursorAdapter?=null
    private  var mCursor: Cursor?=null

    private val SELECTION = MediaStore.Audio.Media.IS_MUSIC + " = ? " + " AND " +
            MediaStore.Audio.Media.MIME_TYPE + " LIKE ? "
    private val SELECTION_ARGS: Array<String> = arrayOf(1.toString(), "audio/mpeg")

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
}