package com.example.healthy_farm

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healthy_farm.com.example.healthy_farm.ProductCart
import com.google.firebase.database.*

class MoveToCartAdapter(private val username:String ,private val productList: ArrayList<ProductCart>) :
    RecyclerView.Adapter<MoveToCartAdapter.ProductViewHolder>() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ordered_products, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productname: TextView = itemView.findViewById(R.id.name)
        private val farmername: TextView = itemView.findViewById(R.id.owner)
        private val price: TextView = itemView.findViewById(R.id.price)
        private val addToCartButton: Button = itemView.findViewById(R.id.order)

        fun bind(product: ProductCart) {
            productname.text = product.productname
            farmername.text = product.farmername
            price.text = product.price

            addToCartButton.setOnClickListener {

//                val username = sharedPref.getString("username","")

                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase.reference.child("cart")

                databaseReference.child("cart").setValue(username)
                    .addOnSuccessListener {
                        Toast.makeText(itemView.context, "Added to cart", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(itemView.context, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                    }

            }
        }
    }
}
