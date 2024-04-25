package com.example.healthy_farm

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class CartView : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<CartDataClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_view)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("cart")

        recyclerView = findViewById(R.id.productcart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf()
//        getUserData()
    }

//    private fun getUserData() {
//
//        val username = sharedPref.getString("username", "")
//        val query = databaseReference.orderByChild("cart")
//
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//
//                    for (productSnapshot in snapshot.children) {
//                        val product = productSnapshot.getValue(CartDataClass::class.java)
//                        arrayList.add(product!!)
//                    }
//
//                    recyclerView.adapter = MoveToCartAdapter(username.toString(),arrayList)
//                }else{
//                    val editText = findViewById<TextView>(R.id.no_products)
//                    editText.setText("NO PRODUCTS FOUND")
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                println("Query canceled: $databaseError")
//            }
//
//        })
//    }
}