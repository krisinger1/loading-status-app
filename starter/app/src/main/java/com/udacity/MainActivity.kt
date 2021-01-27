package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    //private lateinit var intent: Intent
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            Log.i("MainActivity", "in click listener")
            when(radio_group.checkedRadioButtonId){
                radio_button_1.id-> download(URLGlide)
                radio_button_2.id-> download(URLUdacity)
                radio_button_3.id-> download(URLRetrofit)
                else -> {
                    custom_button.buttonState=ButtonState.Completed
                    Toast.makeText(this, "Choose a file to download", Toast.LENGTH_SHORT).show()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "channelName",
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.description = "channel description"

            notificationManager = ContextCompat.getSystemService(applicationContext,
                NotificationManager::class.java) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)
        }



    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            custom_button.buttonState=ButtonState.Completed

            val notificationIntent = Intent(context,MainActivity::class.java)
            val notificationPendingIntent = PendingIntent.getActivity(context,
            0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(REPO_KEY, "repo name")

            pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_description))
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_baseline_cloud_download_24,
                "Check the status", pendingIntent)
                .setAutoCancel(true)

            Toast.makeText(context, "download completed! $id status : ${id == downloadID}", Toast.LENGTH_LONG).show()
            notificationManager.notify(0, builder.build())
        }
    }

    private fun download(url : String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URLUdacity =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URLGlide =
            "https://github.com/bumptech/glide"
        private const val URLRetrofit =
            "https://github.com/square/retrofit"
        private const val CHANNEL_ID = "channelId"

        const val REPO_KEY = "repository"
    }



}
