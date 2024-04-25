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
import com.bumptech.glide.Glide
import com.example.healthy_farm.com.example.healthy_farm.ProductCart

interface OrderClickListener {
    fun onOrderClicked(productCart: ProductCart, quantity: String)
}

class CartAdapter(
    private val productList: ArrayList<ProductCart>,
    private val orderClickListener: OrderClickListener
) : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.cart_products,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = productList[position]

        holder.productname.text = currentitem.productname
        holder.farmername.text = currentitem.farmername
        holder.price.text = currentitem.price
        holder.location.text = currentitem.location
        holder.produce.text = currentitem.produce
//        holder.image.text = currentitem.image
        if (!currentitem.image.isNullOrEmpty()) {
            val imageUrl =
                "https://firebasestorage.googleapis.com/v0/b/healthy-farm-280f0.appspot.com/o/products%2F" + currentitem.image + "?alt=media&token=4529904a-6d66-4d8a-8061-c045aae3bcff"
            Glide.with(holder.itemView.context.applicationContext)
//                .load(currentitem.image)
                .load(imageUrl)
                .into(holder.image)
        } else {
            val packageName = "com.example.healthy_farm"
            val defaultImageUrl =
                Uri.parse("android.resource://${packageName}/${R.drawable.default_veggies}")
            Glide.with(holder.itemView.context.applicationContext)
                .load(defaultImageUrl)
                .into(holder.image)
        }
        holder.order.setOnClickListener {
            val quantity = holder.quantity.text.toString()
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Order Confirmation")
                .setMessage("Are you sure you want to order this item?")
                .setPositiveButton("Yes") { dialog, _ ->
                    // Call interface method when "Yes" is clicked
                    orderClickListener.onOrderClicked(currentitem, quantity)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productname: TextView = itemView.findViewById(R.id.name)
        val farmername: TextView = itemView.findViewById(R.id.owner)
        val price: TextView = itemView.findViewById(R.id.price)
        val location: TextView = itemView.findViewById(R.id.location)
        val produce: TextView = itemView.findViewById(R.id.date)
        val image: ImageView = itemView.findViewById(R.id.image)
        val order: Button = itemView.findViewById(R.id.order)
//        val image : TextView = itemView.findViewById(R.id.image_view)

        val quantity: EditText = itemView.findViewById(R.id.quantity)
    }

}