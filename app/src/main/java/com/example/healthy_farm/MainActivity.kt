package com.example.healthy_farm

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthy_farm.com.example.healthy_farm.ProductCart
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), OrderClickListener {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sharedPref: SharedPreferences

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference1: DatabaseReference

    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<ProductCart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("products")
        databaseReference1 = firebaseDatabase.reference.child("orders")

        recyclerView = findViewById(R.id.productcart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf()
        getProductData()

        val drawerlayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        sharedPref = getSharedPreferences("userdata", MODE_PRIVATE)

        toggle = ActionBarDrawerToggle(this, drawerlayout, R.string.open, R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.nav_orders -> startActivity(Intent(this, OrderByUser::class.java))
                R.id.nav_logout -> {
                    sharedPref.edit().clear().apply()
                    startActivity(Intent(this, LoginPage::class.java))
                    finish()
                    Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    override fun onOrderClicked(productCart: ProductCart, quantity:String) {

        val username = sharedPref.getString("username","")

        databaseReference1.orderByChild("orders").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        val id = databaseReference1.push().key
                        if(quantity.isNotEmpty()) {
                            val orderPlaced = OrderPlace(
                                id,
                                username,
                                productCart.farmername,
                                productCart.productname,
                                quantity
                            )
                            databaseReference1.child(id!!).setValue(orderPlaced)
                            Toast.makeText(
                                this@MainActivity,
                                "Placing order for ${productCart.productname}",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@MainActivity, OrderByUser::class.java))
                            finish()
                        }else{
                            Toast.makeText(this@MainActivity, "Please Select Quantity !!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to Place Order", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseerror: DatabaseError) {
                    Toast.makeText(
                        this@MainActivity,
                        "Database Error:${databaseerror.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

//        Toast.makeText(this, "Placing order for ${productCart.productname}", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getProductData() {

        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(ProductCart::class.java)
                        arrayList.add(product!!)
                    }
                    recyclerView.adapter = CartAdapter(arrayList,this@MainActivity)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Query canceled: $databaseError")
            }

        })
    }

//    private fun putProductdata() {
//        databaseReference.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//
//                    for (productSnapshot in snapshot.children) {
//                        val product = productSnapshot.getValue(ProductCart::class.java)
//                        arrayList.add(product!!)
//                    }
//                    val username = sharedPref.getString("username", "")
//                    if (username != null) {
//                        MoveToCartAdapter(username, arrayList1)
//                    }
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