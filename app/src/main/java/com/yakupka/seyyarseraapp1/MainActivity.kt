package com.yakupka.seyyarseraapp1

//noinspection SuspiciousImport
import android.R
import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yakupka.seyyarseraapp1.databinding.ActivityMainBinding
import java.io.IOException
import java.util.Locale

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingPermission", "CommitPrefEdits", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val shared_editor = sharedPreferences.edit()
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        var btButtonClicked = false
        val BTlistView: ListView = binding.bluetoothListView
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter?.bondedDevices ?: emptySet()
        var deviceListt = ArrayList<String>()
        val music_on = sharedPreferences.getBoolean("music_on", false)
        val hacker_switch_on = sharedPreferences.getBoolean("hacker_switch_on", false)
        val hacker_name = sharedPreferences.getString("hacker_name", "")


        if (hacker_name != "") {
            binding.hackerNameText.text =
                getString(com.yakupka.seyyarseraapp1.R.string.hacker) + " " + hacker_name
        }

        if (!music_on) {
            val intent = Intent(this, MusicService::class.java)
            stopService(intent)
        } else {
            Toast.makeText(
                this,
                getString(com.yakupka.seyyarseraapp1.R.string.forgettenHacker_toast),
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, MusicService::class.java)
            startService(intent)
        }

        if (bluetoothAdapter == null) {
            binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_off)
        } else {
            binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_off)
            if (bluetoothAdapter.isEnabled) {
                binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_on)
            } else {
                binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_off)
            }
        }



        binding.buttonTestButton.setOnClickListener {
            intent = Intent(applicationContext, TestActivity::class.java)
            startActivity(intent)
        }

        binding.main.setOnClickListener {
            btButtonClicked = false
            if (binding.bluetoothView.visibility == View.VISIBLE) {
                binding.bluetoothView.visibility = View.GONE
                binding.mainButtonsView.visibility = View.VISIBLE
            }
        }

        binding.buttonBluetoothImageView.setOnClickListener {
            btButtonClicked = !btButtonClicked

            if (bluetoothAdapter == null) {
                binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_off)
                Toast.makeText(
                    this,
                    getString(com.yakupka.seyyarseraapp1.R.string.unsupportedBluetooth_toast),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_off)
                if (bluetoothAdapter.isEnabled) {
                    binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_on)
                } else {
                    binding.buttonBluetoothImageView.setImageResource(com.yakupka.seyyarseraapp1.R.drawable.ic_bluetooth_off)
                }
            }

            if (bluetoothAdapter != null) {
                if (bluetoothAdapter.isEnabled) {
                    val deviceList = ArrayList<String>()

                    if (btButtonClicked) {
                        binding.bluetoothView.visibility = View.VISIBLE
                        binding.mainButtonsView.visibility = View.GONE

                        if (pairedDevices.isNotEmpty()) {
                            for (device in pairedDevices) {
                                // Cihaz adı ve adresini listeye ekle
                                val deviceName = device.name
                                val deviceAddress = device.address


                                deviceList.add("$deviceName\n$deviceAddress")
                                deviceListt = deviceList
                            }
                        } else {
                            deviceList.add(getString(com.yakupka.seyyarseraapp1.R.string.noPairedDevices_toast))
                        }


                        // ListView'e cihazları bağlamak için ArrayAdapter kullan
                        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, deviceList)
                        binding.bluetoothListView.adapter = adapter

                    } else {
                        binding.bluetoothView.visibility = View.GONE
                        binding.mainButtonsView.visibility = View.VISIBLE
                    }


                } else {
                    if (btButtonClicked == false) {
                        Toast.makeText(
                            this,
                            getString(com.yakupka.seyyarseraapp1.R.string.pleaseOpenBT_toast),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            } else {
                Toast.makeText(
                    this,
                    getString(com.yakupka.seyyarseraapp1.R.string.unsupportedBluetooth_toast),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }


        binding.buttonSettingsImageView.setOnClickListener {
            val rotateAnimator =
                ObjectAnimator.ofFloat(binding.buttonSettingsImageView, "rotation", 0f, 120f)
                    .apply {
                        duration = 150 // 0.15 saniyede bir tam tur
                        interpolator = LinearInterpolator() // Sabit hızda animasyon
                    }

            // Animasyon dinleyicisi ekle
            rotateAnimator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    // Animasyon başlarken bir işlem gerekirse buraya eklenebilir
                }

                override fun onAnimationEnd(animation: Animator) {
                    // Animasyon bittiğinde Settings ekranına geçiş yap
                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(intent)
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            // Animasyonu başlat
            rotateAnimator.start()

        }
        binding.buttonHackerButton.setOnClickListener {
            if (hacker_switch_on) {
                intent = Intent(applicationContext, HackerActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    getString(com.yakupka.seyyarseraapp1.R.string.closedHacker_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.buttonControllerButton.setOnClickListener {
            intent = Intent(applicationContext, ControllerActivity::class.java)
            startActivity(intent)
        }
        binding.buttonInformationsButton.setOnClickListener {
            intent = Intent(applicationContext, InfoActivity::class.java)
            startActivity(intent)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceListt)
        BTlistView.adapter = adapter

        BTlistView.setOnItemClickListener { parent, view, position, id ->
            val selectedDeviceName = parent.getItemAtPosition(position).toString()
            val deviceAddress = selectedDeviceName.substringAfter("\n")
            val deviceName = selectedDeviceName.substringBefore("\n")
            val device = bluetoothAdapter?.getRemoteDevice(deviceAddress)
            shared_editor.putString("DeviceAdress", deviceAddress)
            // Cihaza bağlanma işlemini başlat
            bluetoothAdapter?.cancelDiscovery()
            connectToDevice(device, deviceName)

        }

    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice?, name: String) {

        // Doğru UUID kullanımı
        val socket: BluetoothSocket =
            device?.javaClass?.getMethod("createRfcommSocket", Int::class.java)
                ?.invoke(device, 1) as BluetoothSocket

        try {
            socket.connect()

            SocketManager.bluetoothSocket = socket
            Toast.makeText(
                this,
                name + " " + getString(com.yakupka.seyyarseraapp1.R.string.connectingSucces_toast),
                Toast.LENGTH_SHORT
            ).show()
            binding.bluetoothView.visibility = View.GONE
            binding.mainButtonsView.visibility = View.VISIBLE

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                this,
                getString(com.yakupka.seyyarseraapp1.R.string.connectingUnsucces_toast),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(com.yakupka.seyyarseraapp1.R.string.exApp_toast))
        builder.setMessage(getString(com.yakupka.seyyarseraapp1.R.string.exAppQuest_toast))
        builder.setPositiveButton(getString(com.yakupka.seyyarseraapp1.R.string.yes)) { _, _ ->
            System.exit(0)
            super.onBackPressed() // Varsayılan davranışı devam ettir

        }
        builder.setNegativeButton(getString(com.yakupka.seyyarseraapp1.R.string.no)) { dialog, _ ->
            dialog.dismiss() // Diyaloğu kapat
        }
        builder.show()
    }


}



