package com.example.healthy_farm

import android.app.AlertDialog
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class OrderAdapter(
    private val orderList: ArrayList<OrderPlace>
) : RecyclerView.Adapter<OrderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.ordered_products,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = orderList[position]

        holder.username.text = currentitem.username
        holder.productname.text = currentitem.productname
        holder.farmername.text = currentitem.farmername
        holder.quantity.text = currentitem.quantity
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val username: TextView = itemView.findViewById(R.id.user)
        val productname: TextView = itemView.findViewById(R.id.name)
        val farmername: TextView = itemView.findViewById(R.id.owner)
        val quantity: TextView = itemView.findViewById(R.id.qty)
    }

}