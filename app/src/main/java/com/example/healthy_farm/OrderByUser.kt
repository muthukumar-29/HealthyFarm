package com.example.healthy_farm

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class OrderByUser : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sharedPref: SharedPreferences

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderList: ArrayList<OrderPlace>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_by_user)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("orders")

        recyclerView = findViewById(R.id.productcart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        orderList = arrayListOf()
        getProductData()

    }

    private fun getProductData() {
        databaseReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        for (productSnapshot in snapshot.children) {
                            val orders = productSnapshot.getValue(OrderPlace::class.java)
                            orderList.add(orders!!)
                        }
                        recyclerView.adapter = OrderAdapter(orderList)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Query canceled: $databaseError")
                }

            })
    }
}