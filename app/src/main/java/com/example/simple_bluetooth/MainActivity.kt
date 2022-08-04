package com.example.simple_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.simple_bluetooth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    private val REQUEST_ENABLE_BT = 1
    private var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        binding.BTSearch.setOnClickListener {
            bluetoothcheck()
        }

    }

    @SuppressLint("MissingPermission")
    fun bluetoothcheck() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "블루투스를 지원하지않습니다.", Toast.LENGTH_SHORT).show()
        } else {
            if (bluetoothAdapter?.isEnabled == false) {
                Toast.makeText(this, "블루투스를 켭니다", Toast.LENGTH_SHORT).show()
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                // search
            }
        }
    }
}