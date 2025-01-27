package com.yakupka.seyyarseraapp1

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        // Müzik dosyasını başlat
        mediaPlayer = MediaPlayer.create(this, R.raw.music) // raw klasörüne eklediğin dosyanın adı
        mediaPlayer.isLooping = true // Müzik döngüye girsin mi?
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer.start() // Müzik çalmaya başlar
        return START_STICKY // Servisin uygulama kapansa bile devam etmesini sağlar
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
