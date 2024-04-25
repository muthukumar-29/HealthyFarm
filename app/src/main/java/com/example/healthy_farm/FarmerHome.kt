package com.example.healthy_farm

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*

class FarmerHome : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sharedPref: SharedPreferences

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var arrayList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer_home)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("products")

        sharedPref = getSharedPreferences("userdata", MODE_PRIVATE)

        recyclerView = findViewById(R.id.productlist)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        arrayList = arrayListOf()
        getUserData()


        val drawerlayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.farmer_nav_view)

        val add: FloatingActionButton = findViewById(R.id.add_product)

        add.setOnClickListener {
            val intent = Intent(this@FarmerHome, AddProduct::class.java)
            startActivity(intent)
        }

        toggle = ActionBarDrawerToggle(this, drawerlayout, R.string.open, R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.farm_home -> startActivity(Intent(this@FarmerHome, FarmerHome::class.java))
                R.id.farm_add_product -> startActivity(
                    Intent(
                        this@FarmerHome,
                        AddProduct::class.java
                    )
                )
                R.id.farm_view_product -> startActivity(
                    Intent(
                        this@FarmerHome,
                        ViewProducts::class.java
                    )
                )
                R.id.farm_orders -> startActivity(
                    Intent(
                        this@FarmerHome,
                        OrderManage::class.java
                    )
                )
                R.id.farm_logout -> {
                    sharedPref.edit().clear().apply()
                    sharedPref.edit().clear().apply()
                    startActivity(Intent(this, LoginPage::class.java))
                    finish()
                    Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getUserData() {

        val username = sharedPref.getString("username", "")
        val query = databaseReference.orderByChild("farmername").equalTo(username)

        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (productSnapshot in snapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        arrayList.add(product!!)
                    }

                    recyclerView.adapter = MyAdapter(arrayList)
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
