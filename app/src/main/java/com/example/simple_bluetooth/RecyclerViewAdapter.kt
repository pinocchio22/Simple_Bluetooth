package com.example.simple_bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author CHOI
 * @email vviian.2@gmail.com
 * @created 2022-08-04
 * @desc
 */
class RecyclerViewAdapter(private val myDataset: ArrayList<BluetoothDevice>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    class MyViewHolder(val linearView: LinearLayout) : RecyclerView.ViewHolder(linearView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val linearView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false) as LinearLayout
        return MyViewHolder(linearView)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val Name: TextView = holder.linearView.findViewById(R.id.item_name)
        val Adress: TextView = holder.linearView.findViewById(R.id.item_address)
        Name.text = myDataset[position].name
        Adress.text = myDataset[position].address
    }

    override fun getItemCount() = myDataset.size

}