package com.yakupka.seyyarseraapp1

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.content.ContextCompat
import com.yakupka.seyyarseraapp1.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding
    lateinit var sharedPreferences: SharedPreferences


    @SuppressLint("MissingPermission", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val shared_editor = sharedPreferences.edit()
        var hacker_on = sharedPreferences.getBoolean("hacker_on", false)
        var password_hack = sharedPreferences.getString("password_hack", "Ruhi123")
        var hacker_switch_on = sharedPreferences.getBoolean("hacker_switch_on", false)
        var down_code: String = sharedPreferences.getString("Controller_DownCode", "Aşağı").toString()
        var up_code: String = sharedPreferences.getString("Controller_UpCode", "Yukarı").toString()
        var left_code: String = sharedPreferences.getString("Controller_LeftCode", "Sola").toString()
        var right_code: String = sharedPreferences.getString("Controller_RightCode", "Sağa").toString()

        window.navigationBarColor = ContextCompat.getColor(this, com.yakupka.seyyarseraapp1.R.color.black)

        binding.downButtonDataEditText.hint = down_code
        binding.upButtonDataEditText.hint = up_code
        binding.leftButtonDataEditText.hint = left_code
        binding.rightButtonDataEditText.hint = right_code

        binding.hackerView.setOnClickListener {
            if (!hacker_on){
                binding.passwordViewLinearLayout.visibility= View.VISIBLE
            }
            if (binding.controllerDataViewLinearLayout.visibility == View.VISIBLE){
                binding.controllerDataViewLinearLayout.visibility = View.GONE
            }
        }

        binding.passwordViewLinearLayout.setOnClickListener {
            binding.passwordViewLinearLayout.visibility= View.VISIBLE
        }

        if (bluetoothAdapter == null) {
            binding.bluetoothStateSwitch.isChecked = false
            binding.bluetoothStateSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb_disabled)
            binding.bluetoothStateSwitch.trackDrawable = getDrawable(R.drawable.switch_track_disabled)

        } else {
            binding.bluetoothStateSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb)
            binding.bluetoothStateSwitch.trackDrawable = getDrawable(R.drawable.switch_track)
        }

        binding.donePasswordButton.setOnClickListener {
            var password = binding.passwordInputEditText.text.toString()
            var name = binding.nameInputEditText.text.toString()

            if (password == "" && name == "") {
                Toast.makeText(this, getString(R.string.plsFillAllSpc_toast), Toast.LENGTH_SHORT).show()
            }
            else if(name == ""){
                Toast.makeText(this, getString(R.string.plsFillNameSpc_toast), Toast.LENGTH_SHORT).show()
            }
            else if(password == "" ){
                Toast.makeText(this, getString(R.string.plsFillPasswordSpc_toast_toast), Toast.LENGTH_SHORT).show()
            }else{
                if (password == password_hack) {
                    hacker_on = true
                    shared_editor.putBoolean("hacker_on", true)
                    shared_editor.apply()
                    hacker_switch_on = true
                    shared_editor.putBoolean("hacker_switch_on", true)
                    shared_editor.apply()
                    Toast.makeText(
                        this,
                        getString(R.string.truePassw_toast)+" "+name,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.hackerStateSwitch.isChecked = false
                    binding.hackerStateSwitch.visibility = View.VISIBLE
                    binding.passwordViewLinearLayout.visibility = View.GONE
                    binding.hackerStateSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb)
                    binding.hackerStateSwitch.trackDrawable = getDrawable(R.drawable.switch_track)
                    binding.hackerStateSwitch.isChecked = true
                    binding.LinearLayout3.isClickable = false
                    shared_editor.putString("hacker_name",name)
                    shared_editor.apply()

                } else {
                    Toast.makeText(this, getString(R.string.falsePassw_toast), Toast.LENGTH_SHORT).show()
                }
            }

        }

        if (!hacker_on) {
            binding.hackerStateSwitch.visibility = View.GONE
            binding.hackerStateSwitch.isChecked = true
            binding.hackerStateSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb_disabled)
            binding.hackerStateSwitch.trackDrawable = getDrawable(R.drawable.switch_track_disabled)
        } else {
            binding.passwordViewLinearLayout.visibility = View.GONE
            binding.hackerStateSwitch.visibility = View.VISIBLE
            binding.hackerStateSwitch.thumbDrawable = getDrawable(R.drawable.switch_thumb)
            binding.hackerStateSwitch.trackDrawable = getDrawable(R.drawable.switch_track)
            if (!hacker_switch_on) {
                binding.hackerStateSwitch.isChecked = false
            } else {
                binding.hackerStateSwitch.isChecked = true
            }


        }


        binding.hackerStateSwitch.setOnClickListener {
            if (binding.hackerStateSwitch.isChecked) {
                hacker_switch_on = true
                shared_editor.putBoolean("hacker_switch_on", true)
                shared_editor.apply()
            } else {
                hacker_switch_on = false
                shared_editor.putBoolean("hacker_switch_on", false)
                shared_editor.apply()
            }

        }

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                binding.bluetoothStateSwitch.isChecked = true
            } else {
                binding.bluetoothStateSwitch.isChecked = false
            }
        }

        binding.controllerDatasTextView.setOnClickListener {
            if (binding.controllerDataViewLinearLayout.visibility != View.VISIBLE){
                binding.controllerDataViewLinearLayout.visibility = View.VISIBLE
            }else{
                binding.controllerDataViewLinearLayout.visibility = View.GONE
            }
            if (binding.passwordViewLinearLayout.visibility == View.VISIBLE){
                binding.passwordViewLinearLayout.visibility = View.GONE
            }

        }
        binding.main.setOnClickListener {
            binding.controllerDataViewLinearLayout.visibility = View.GONE
            binding.passwordViewLinearLayout.visibility = View.GONE
        }
        binding.doneControllerDatasButton.setOnClickListener {
            binding.controllerDataViewLinearLayout.visibility = View.GONE
        }
        binding.controllerDataViewLinearLayout.setOnClickListener {
            binding.controllerDataViewLinearLayout.visibility = View.VISIBLE
        }

        binding.doneControllerDatasButton.setOnClickListener {
            if (binding.downButtonDataEditText.text.isNotEmpty() && binding.downButtonDataEditText.text.toString() != " "){
                shared_editor.putString("Controller_DownCode",binding.downButtonDataEditText.text.toString()).apply()
                down_code = binding.downButtonDataEditText.text.toString()
                binding.downButtonDataEditText.hint = down_code
                binding.downButtonDataEditText.text.clear()
            }else if(binding.downButtonDataEditText.text.toString() != " "){
                binding.downButtonDataEditText.hint = down_code
                binding.downButtonDataEditText.text.clear()
            }
            if (binding.upButtonDataEditText.text.isNotEmpty() && binding.upButtonDataEditText.text.toString() != " "){
                shared_editor.putString("Controller_UpCode",binding.upButtonDataEditText.text.toString()).apply()
                up_code = binding.upButtonDataEditText.text.toString()
                binding.upButtonDataEditText.hint = up_code
                binding.upButtonDataEditText.text.clear()
            }else if(binding.upButtonDataEditText.text.toString() == " "){
                binding.upButtonDataEditText.hint = up_code
                binding.upButtonDataEditText.text.clear()
            }
            if (binding.leftButtonDataEditText.text.isNotEmpty() && binding.leftButtonDataEditText.text.toString() != " "){
                shared_editor.putString("Controller_LeftCode",binding.leftButtonDataEditText.text.toString()).apply()
                left_code = binding.leftButtonDataEditText.text.toString()
                binding.leftButtonDataEditText.hint = left_code
                binding.leftButtonDataEditText.text.clear()
            }else if(binding.leftButtonDataEditText.text.toString() == " "){
                binding.leftButtonDataEditText.hint = left_code
                binding.leftButtonDataEditText.text.clear()
            }
            if (binding.rightButtonDataEditText.text.isNotEmpty() && binding.rightButtonDataEditText.text.toString() != " "){
                shared_editor.putString("Controller_RightCode",binding.rightButtonDataEditText.text.toString()).apply()
                right_code = binding.rightButtonDataEditText.text.toString()
                binding.rightButtonDataEditText.hint = right_code
                binding.rightButtonDataEditText.text.clear()
            }else if(binding.rightButtonDataEditText.text.toString() == " "){
                binding.rightButtonDataEditText.hint = right_code
                binding.rightButtonDataEditText.text.clear()
            }
            Toast.makeText(this,getString(R.string.recorded),Toast.LENGTH_SHORT).show()
        }

        binding.aboutUsLinearLayout.setOnClickListener {
            intent = Intent(applicationContext, InformationAboutUsActivity::class.java)
            startActivity(intent)
        }
        binding.bluetoothStateSwitch.setOnClickListener {
            if (bluetoothAdapter != null) {

                if (binding.bluetoothStateSwitch.isChecked == true) {

                    val progressDialog = ProgressDialog(this)
                    progressDialog.setMessage("Yükleniyor...")
                    progressDialog.setCancelable(false)
                    progressDialog.setCanceledOnTouchOutside(true)
                    progressDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressDialog.dismiss()
                        bluetoothAdapter.enable()
                        Toast.makeText(this, getString(R.string.bluettothOpened_toast), Toast.LENGTH_SHORT).show()
                    }, 1000)
                } else {

                    val progressDialog = ProgressDialog(this)
                    progressDialog.setMessage("Yükleniyor...")
                    progressDialog.setCancelable(false)
                    progressDialog.setCanceledOnTouchOutside(true)
                    progressDialog.show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        progressDialog.dismiss()
                        bluetoothAdapter.disable()
                        Toast.makeText(this, getString(R.string.bluettothClosed_toast), Toast.LENGTH_SHORT).show()
                    }, 1000)
                }
            } else {
                binding.bluetoothStateSwitch.isChecked = false
                Toast.makeText(this, getString(R.string.unsupportedBluetooth_toast), Toast.LENGTH_LONG)
                    .show()
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
