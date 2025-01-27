package com.yakupka.seyyarseraapp1


import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yakupka.seyyarseraapp1.databinding.ActivityHackerBinding

class HackerActivity : AppCompatActivity() {
    lateinit var binding: ActivityHackerBinding
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val shared_editor = sharedPreferences.edit()
        var music_on = sharedPreferences.getBoolean("music_on", false)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this,R.color.black)

        if (music_on) {
            binding.hackerButtonImageView.setImageResource(R.drawable.start)
        } else {
            binding.hackerButtonImageView.setImageResource(R.drawable.pause)
        }

        binding.hackerButtonImageView.setOnClickListener {

            if (music_on) {
                val intent = Intent(this, MusicService::class.java)
                stopService(intent)
                binding.hackerButtonImageView.setImageResource(R.drawable.pause)
                music_on = false
                shared_editor.putBoolean("music_on", false)
                shared_editor.apply()
            } else {
                val intent = Intent(this, MusicService::class.java)
                startService(intent)
                binding.hackerButtonImageView.setImageResource(R.drawable.start)
                music_on = true
                shared_editor.putBoolean("music_on", true)
                shared_editor.apply()
            }

        }


    }
    override fun onBackPressed() {
        intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

}
