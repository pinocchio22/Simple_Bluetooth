package com.example.simple_bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simple_bluetooth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    private val REQUEST_ENABLE_BT = 1
    private val REQUEST_ALL_PERMISSIONS = 2
    private val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    private var bluetoothAdapter: BluetoothAdapter? = null

    private var scanning: Boolean = false
    private var devicesArr = ArrayList<BluetoothDevice>()
    private val SCAN_PERIOD = 1000
    private val handler = Handler()

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter

    private val mLeScanCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("scanCallback", "BLE Scan FAiled : $errorCode")
        }
        @SuppressLint("MissingPermission")
        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            results?.let {
                for (result in it) {
                    if (!devicesArr.contains(result.device) && result.device.name != null) {
                        devicesArr.add(result.device)
                    }
                }
            }
        }
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let {
                if (!devicesArr.contains(result.device) && result.device.name != null) {
                    devicesArr.add(it.device)
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        viewManager = LinearLayoutManager(this)
        recyclerViewAdapter =  RecyclerViewAdapter(devicesArr)
        binding.recyclerView.apply {
            layoutManager = viewManager
            adapter = recyclerViewAdapter
        }
        binding.BTSearch.setOnClickListener {
            bluetoothcheck()
        }

    }

    fun hasPermissions(context: Context?, permissions: Array<String>) : Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ALL_PERMISSIONS -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permissions granted!", Toast.LENGTH_LONG).show()
                    } else {
                        requestPermissions(permissions, REQUEST_ALL_PERMISSIONS)
                        Toast.makeText(this, "Permissions must be granted", Toast.LENGTH_SHORT).show()
                    }
                }
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
                if (!hasPermissions(this, PERMISSIONS)) {
                    requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSIONS)
                }
                scanDevice(true)
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun scanDevice(state: Boolean) = if (state) {
        handler.postDelayed({
            scanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(mLeScanCallback)
        }, SCAN_PERIOD)
        scanning = true
        devicesArr.clear()
        bluetoothAdapter?.bluetoothLeScanner?.startScan(mLeScanCallback)
    } else {
        scanning = false
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(mLeScanCallback)
    }
}

private fun Handler.postDelayed(function: () -> Unit?, scanPeriod: Int) {

}