package com.yakupka.seyyarseraapp1

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yakupka.seyyarseraapp1.SocketManager.bluetoothSocket
import com.yakupka.seyyarseraapp1.databinding.ActivityInfoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

class InfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfoBinding
    private var BTopened = false
    lateinit var air_temp: String
    lateinit var air_hum: String
    lateinit var soil_hum: String
    lateinit var ldr1: String
    lateinit var ldr2: String
    lateinit var battery: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = ContextCompat.getColor(this, com.yakupka.seyyarseraapp1.R.color.black)

        checkBluetoothConnections(this)

        // Coroutine ile veri okuma işlemini arka planda başlatıyoruz
        if (BTopened) {
            GlobalScope.launch(Dispatchers.IO) {
                while (BTopened) {
                    val data = readData(bluetoothSocket?.inputStream)
                    if (data != null) {
                        // Veriyi doğru şekilde ayırıp UI'yi güncelliyoruz
                        val words = data.split(" ")  // Burada "," ayırıcı kullanılıyor
                        air_temp = words.getOrNull(1).toString()
                        air_hum = words.getOrNull(3).toString()
//                        soil_hum = words.getOrNull(2).toString()
//                        ldr1 = words.getOrNull(3).toString()
//                        ldr2 = words.getOrNull(4).toString()
//                        battery = words.getOrNull(5).toString()

                        // UI güncelleme işlemi main thread üzerinde yapılmalı
                        runOnUiThread {
                            binding.airTempData.text = "$air_temp C°"
                            binding.airHumData.text = "%$air_hum"
                            binding.soilMastureData.text = soil_hum
                            binding.LDR1Data.text = ldr1
                            binding.LDR2Data.text = ldr2
                            binding.batteryPercentData.text = "%$battery"
                        }
                    }
                    delay(1000) // 1 saniye bekle, sürekli okuma yapma
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.noConnectedDevices_toast_toast), Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData(inputStream: InputStream?): String? {
        try {
            val buffer = ByteArray(1024)
            val bytesRead = inputStream?.read(buffer)

            if (bytesRead != null && bytesRead > 0) {
                return String(buffer, 0, bytesRead)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("MissingPermission")
    private fun checkBluetoothConnections(context: Context) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            Toast.makeText(context, getString(R.string.unsupportedBluetooth_toast), Toast.LENGTH_SHORT).show()
            return
        } else if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(context, getString(R.string.closedBT_toast), Toast.LENGTH_SHORT).show()
            return
        } else {
            if (bluetoothSocket?.isConnected == true) {
                BTopened = true
            } else {
                BTopened = false
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
