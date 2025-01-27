package com.yakupka.seyyarseraapp1


import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yakupka.seyyarseraapp1.SocketManager.bluetoothSocket
import com.yakupka.seyyarseraapp1.databinding.ActivityControllerBinding
import java.io.OutputStream
import java.util.Locale

class ControllerActivity : AppCompatActivity() {
    lateinit var binding: ActivityControllerBinding
    lateinit var sharedPreferences: SharedPreferences
    private var BTopened = false


    @SuppressLint("ClickableViewAccessibility", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val shared_editor = sharedPreferences.edit()
        var arrow_buttons_clickable = sharedPreferences.getBoolean("arrows_clickable", false)
        val bluetoothSocket = SocketManager.bluetoothSocket
        val outputStream = bluetoothSocket?.outputStream
        val down_code: String = sharedPreferences.getString("Controller_DownCode", getString(R.string.downButtonData_editText_text)).toString()
        val up_code: String = sharedPreferences.getString("Controller_UpCode", getString(R.string.upButtonData_editText_text)).toString()
        val left_code: String = sharedPreferences.getString("Controller_LeftCode", getString(R.string.leftButtonData_editText_text)).toString()
        val right_code: String = sharedPreferences.getString("Controller_RightCode", getString(R.string.rightButtonData_editText_text)).toString()

        window.navigationBarColor = ContextCompat.getColor(this, com.yakupka.seyyarseraapp1.R.color.black)

        onBackPressedDispatcher.addCallback {
            intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }
        changeLanguage("")

        checkBluetoothConnections(this)

        if (BTopened) {
            binding.controllerSwitcherSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb)
            binding.controllerSwitcherSwitch.trackDrawable = getDrawable(R.drawable.switch_track)

            if (sharedPreferences.getBoolean("arrows_clickable", false)) {
                binding.controllerLabelTextView.text =
                    getString(R.string.controllerLabel_textView_text1)
                binding.controllerSwitcherSwitch.isChecked = true
                binding.upButtonImageView.animate().alpha(1.0F).setDuration(500)
                binding.downButtonImageView.animate().alpha(1.0F).setDuration(500)
                binding.leftButtonImageView.animate().alpha(1.0F).setDuration(500)
                binding.rightButtonImageView.animate().alpha(1.0F).setDuration(500)
            } else {
                binding.controllerLabelTextView.text =
                    getString(R.string.controllerLabel_textView_text0)
                binding.controllerSwitcherSwitch.isChecked = false
                binding.upButtonImageView.animate().alpha(0.5F).setDuration(500)
                binding.downButtonImageView.animate().alpha(0.5F).setDuration(500)
                binding.leftButtonImageView.animate().alpha(0.5F).setDuration(500)
                binding.rightButtonImageView.animate().alpha(0.5F).setDuration(500)
            }

        } else {
            binding.controllerSwitcherSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb_disabled)
            binding.controllerSwitcherSwitch.trackDrawable = getDrawable(R.drawable.switch_track_disabled)
            binding.controllerLabelTextView.text =
                getString(R.string.controllerLabel_textView_text0)
            binding.controllerSwitcherSwitch.isChecked = false
            binding.upButtonImageView.animate().alpha(0.5F).setDuration(500)
            binding.downButtonImageView.animate().alpha(0.5F).setDuration(500)
            binding.leftButtonImageView.animate().alpha(0.5F).setDuration(500)
            binding.rightButtonImageView.animate().alpha(0.5F).setDuration(500)
        }


        binding.controllerSwitcherSwitch.setOnClickListener {
            checkBluetoothConnections(this)
            if (BTopened == true) {
                binding.controllerSwitcherSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb)
                binding.controllerSwitcherSwitch.trackDrawable = getDrawable(R.drawable.switch_track)
                binding.controllerSwitcherSwitch.isClickable = true
                if (binding.controllerSwitcherSwitch.isChecked) {

                    binding.controllerLabelTextView.text =
                        getString(R.string.controllerLabel_textView_text1)
                    binding.controllerSwitcherSwitch.isClickable = true
                    binding.upButtonImageView.animate().alpha(1.0F).setDuration(500)
                    binding.downButtonImageView.animate().alpha(1.0F).setDuration(500)
                    binding.leftButtonImageView.animate().alpha(1.0F).setDuration(500)
                    binding.rightButtonImageView.animate().alpha(1.0F).setDuration(500)

                    arrow_buttons_clickable = true

                    shared_editor.putBoolean("arrows_clickable", true)
                    shared_editor.apply()
                    Toast.makeText(this, getString(com.yakupka.seyyarseraapp1.R.string.controllerOpened_toast), Toast.LENGTH_SHORT).show()

                } else {
                    binding.controllerLabelTextView.text =
                        getString(R.string.controllerLabel_textView_text0)
                    binding.upButtonImageView.animate().alpha(0.5F).setDuration(500)
                    binding.downButtonImageView.animate().alpha(0.5F).setDuration(500)
                    binding.leftButtonImageView.animate().alpha(0.5F).setDuration(500)
                    binding.rightButtonImageView.animate().alpha(0.5F).setDuration(500)
                    arrow_buttons_clickable = false
                    shared_editor.putBoolean("arrows_clickable", false)
                    shared_editor.apply()
                    Toast.makeText(this, getString(com.yakupka.seyyarseraapp1.R.string.controllerClosed_toast), Toast.LENGTH_SHORT).show()

                }
            } else {
                binding.controllerSwitcherSwitch.isChecked = false
                binding.controllerSwitcherSwitch.isClickable = false
                binding.upButtonImageView.animate().alpha(0.5F).setDuration(500)
                binding.downButtonImageView.animate().alpha(0.5F).setDuration(500)
                binding.leftButtonImageView.animate().alpha(0.5F).setDuration(500)
                binding.rightButtonImageView.animate().alpha(0.5F).setDuration(500)
                arrow_buttons_clickable = false
                shared_editor.putBoolean("arrows_clickable", false)
                shared_editor.apply()
                Toast.makeText(this, getString(com.yakupka.seyyarseraapp1.R.string.noConnectedDevices_toast_toast), Toast.LENGTH_SHORT).show()
            }

        }


        binding.downButtonImageView.setOnTouchListener { v, event ->
            if (arrow_buttons_clickable && BTopened) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        sendData(down_code, outputStream)
                        // Dokunulduğunda buton arka planını değiştirelim
                        binding.downButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_blue)

                        // Titreşim ekle
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    100,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            vibrator.vibrate(100)
                        }
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        // Dokunma bırakıldığında buton arka planını eski haline getirelim
                        binding.downButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_white)
                    }
                }
            }
            true
        }
        binding.upButtonImageView.setOnTouchListener { v, event ->
            if (arrow_buttons_clickable && BTopened) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        sendData(up_code, outputStream)
                        binding.upButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_blue)
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    100,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            vibrator.vibrate(100)
                        }


                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        binding.upButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_white)

                    }
                }
            }
            true
        }

        binding.leftButtonImageView.setOnTouchListener { v, event ->
            if (arrow_buttons_clickable && BTopened) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        sendData(left_code, outputStream)
                        binding.leftButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_blue)

                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    100,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            vibrator.vibrate(100)
                        }


                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        binding.leftButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_white)

                    }
                }
            }
            true
        }
        binding.rightButtonImageView.setOnTouchListener { v, event ->
            if (arrow_buttons_clickable && BTopened) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        sendData(right_code, outputStream)
                        binding.rightButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_blue)
                        if (Build.VERSION.SDK_INT >= 26) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    100,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            vibrator.vibrate(100)
                        }

                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        binding.rightButtonImageView.setBackgroundResource(R.drawable.rounded_controller_button_white)

                    }
                }
            }
            true
        }

    }

    private fun sendData(data: String, outputStream: OutputStream?) {
        try {
            outputStream?.write(data.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkBluetoothConnections(context: Context) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        // Bluetooth desteklenmiyor kontrolü
        if (bluetoothAdapter == null) {
            Toast.makeText(context, getString(com.yakupka.seyyarseraapp1.R.string.unsupportedBluetooth_toast), Toast.LENGTH_SHORT).show()
            return
        }

        // Bluetooth kapalıysa uyarı ver
        else if (!bluetoothAdapter.isEnabled) {
            Toast.makeText(context, getString(com.yakupka.seyyarseraapp1.R.string.closedBT_toast), Toast.LENGTH_SHORT).show()
            return
        } else {
            if (bluetoothSocket?.isConnected == true) {
                BTopened = true
            } else {
                BTopened = false
            }
        }

    }
    private fun changeLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
    }

}







