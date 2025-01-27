package com.yakupka.seyyarseraapp1

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.yakupka.seyyarseraapp1.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.navigationBarColor = ContextCompat.getColor(this, com.yakupka.seyyarseraapp1.R.color.black)


        binding.startTestButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.testStarted_toast), Toast.LENGTH_SHORT).show()
            showNotification(getString(R.string.testStarted_toast), getString(R.string.warning))
        }

    }

    @SuppressLint("NotificationPermission", "MissingPermission")
    fun showNotification(contentText: String, contentTitle: String) {
        val channel = NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, "n")
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.notification_picture)
            .setAutoCancel(true)

        val managerCompat = NotificationManagerCompat.from(this)

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(contentIntent)

        managerCompat.notify(999, builder.build())


    }
    override fun onBackPressed() {
        intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }
}
