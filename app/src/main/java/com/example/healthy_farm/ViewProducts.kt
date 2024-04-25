package com.example.healthy_farm

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ViewProducts : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<ProductEdit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_products)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("products")

        recyclerView = findViewById(R.id.productedit)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf()
        getUserData()

    }
    private fun getUserData() {

        sharedPref = getSharedPreferences("userdata", MODE_PRIVATE)
        val username = sharedPref.getString("username", "")
        val query = databaseReference.orderByChild("farmername").equalTo(username)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(ProductEdit::class.java)
                        arrayList.add(product!!)
                    }

                    recyclerView.adapter = EditAdapter(arrayList)
                }else{
                    val editText = findViewById<TextView>(R.id.no_products)
                    editText.setText("NO PRODUCTS FOUND")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Query canceled: $databaseError")
            }

        })

    }
}