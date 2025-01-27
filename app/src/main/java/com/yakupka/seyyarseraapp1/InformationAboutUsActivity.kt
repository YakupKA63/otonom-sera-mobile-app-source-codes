package com.yakupka.seyyarseraapp1

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yakupka.seyyarseraapp1.databinding.ActivityInformationAboutUsBinding

class InformationAboutUsActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var binding: ActivityInformationAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformationAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback {
            intent = Intent(applicationContext,SettingsActivity::class.java)
            startActivity(intent)
        }
        window.navigationBarColor = ContextCompat.getColor(this, com.yakupka.seyyarseraapp1.R.color.black)

        binding.mailButton.setOnClickListener {
            mediaPlayer =
                MediaPlayer.create(this, R.raw.click) // raw klasörüne eklediğin dosyanın adı
            mediaPlayer.isLooping = false
            mediaPlayer.start()
            val recipient = arrayOf("kucukaslanyakup@gmail.com") // Alıcı e-posta adresi
            val subject = "Otonom Sera App Hakkında" // Konu
            val body = """
                Merhaba!
                Geliştirdiğiniz mobil uygulama olan Otonom Sera Mobile App'i belli bir süre kullandım.
                Uygulama hakkında şunları söylemek isterim:
                
            """.trimIndent()

            // Intent oluştur
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822" // Sadece e-posta uygulamalarını hedefler
                putExtra(Intent.EXTRA_EMAIL, recipient) // Alıcı
                putExtra(Intent.EXTRA_SUBJECT, subject) // Konu
                putExtra(Intent.EXTRA_TEXT, body) // Gövde
            }

            // Eğer uygun bir uygulama varsa aç
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Eğer cihazda e-posta uygulaması yoksa kullanıcıya bilgi ver
                Toast.makeText(this,getString(R.string.noEmailApp_toast),Toast.LENGTH_SHORT).show()
            }
        }
        binding.webButton.setOnClickListener {
            mediaPlayer =
                MediaPlayer.create(this, R.raw.click) // raw klasörüne eklediğin dosyanın adı
            mediaPlayer.isLooping = false
            mediaPlayer.start()
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    }
}