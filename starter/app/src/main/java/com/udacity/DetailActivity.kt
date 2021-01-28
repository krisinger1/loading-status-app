package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        //get data from intent extras
        filename_textview.text=intent.getStringExtra(MainActivity.FILE_KEY)
        status_textview.text=intent.getStringExtra(MainActivity.STATUS_KEY)

        //cancel the notification
        val notificationManager = ContextCompat.getSystemService(applicationContext,
                NotificationManager::class.java) as NotificationManager
        notificationManager.cancel(intent.getIntExtra(MainActivity.NOTIFICATION_ID_KEY,0))

        //use ok button to navigate back to MainActivity
        ok_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
