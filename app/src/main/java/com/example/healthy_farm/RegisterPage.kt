package com.example.healthy_farm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.healthy_farm.databinding.ActivityRegisterPageBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class RegisterPage : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference1: DatabaseReference
    private var userType: String = ""

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        databaseReference1 = firebaseDatabase.reference.child("farmers")

        firebaseAuth = FirebaseAuth.getInstance()

        val editUname = findViewById<EditText>(R.id.name)
        val editEmail = findViewById<EditText>(R.id.email)
        val editMobile = findViewById<EditText>(R.id.phone)
        val editAddress = findViewById<EditText>(R.id.address)
        val editDistrict = findViewById<EditText>(R.id.district)
        val editState = findViewById<EditText>(R.id.State)
        val editPincode = findViewById<EditText>(R.id.pincode)
        val editPassword = findViewById<EditText>(R.id.password)
        val editConfirmPass = findViewById<EditText>(R.id.cpass)
        val editButton = findViewById<Button>(R.id.submit)

        //Get type of User
        val spinner = findViewById<Spinner>(R.id.type_drop)
        val items = listOf("Farmer", "Customer")
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    userType = items[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(this@RegisterPage, "Please Select User", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        //Move to register page
        val textLogin = findViewById<TextView>(R.id.login)
        textLogin.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        //Get Data from user and validate
        editButton.setOnClickListener {
            val uname = editUname.text.toString()
            val email = editEmail.text.toString()
            val mobile = editMobile.text.toString()
            val address = editAddress.text.toString()
            val district = editDistrict.text.toString()
            val state = editState.text.toString()
            val pincode = editPincode.text.toString()
            val type = userType
            val password = editPassword.text.toString()
            val cpass = editConfirmPass.text.toString()

            if (uname.isNotEmpty() && email.isNotEmpty() && mobile.isNotEmpty() && address.isNotEmpty() && district.isNotEmpty() && state.isNotEmpty() && pincode.isNotEmpty() && password.isNotEmpty() && cpass.isNotEmpty() && password.isNotEmpty()) {
                if (password == cpass) {
                    if (type == "Customer") {
                        registerUser(
                            uname,
                            email,
                            mobile,
                            address,
                            district,
                            state,
                            pincode,
                            type,
                            password
                        )
                    } else {
                        registerFarmer(
                            uname,
                            email,
                            mobile,
                            address,
                            district,
                            state,
                            pincode,
                            type,
                            password
                        )
                    }
                } else {
                    Toast.makeText(this@RegisterPage, "Doesn't Match Password", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this@RegisterPage, "All fields are mandatory", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun registerUser(
        username: String,
        email: String,
        mobile: String,
        address: String,
        district: String,
        state: String,
        pincode: String,
        type: String,
        password: String,
    ) {
        databaseReference.orderByChild("users").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        val id = databaseReference.push().key
                        val userData = UserData(
                            id,
                            username,
                            email,
                            mobile,
                            address,
                            district,
                            state,
                            pincode,
                            type,
                            password
                        )
                        databaseReference.child(id!!).setValue(userData)
                        Toast.makeText(
                            this@RegisterPage, "Register Successfully", Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@RegisterPage, LoginPage::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterPage, "Already Exists", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseerror: DatabaseError) {
                    Toast.makeText(
                        this@RegisterPage,
                        "Database Error:${databaseerror.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun registerFarmer(
        username: String,
        email: String,
        mobile: String,
        address: String,
        district: String,
        state: String,
        pincode: String,
        type: String,
        password: String,
    ) {
        databaseReference1.orderByChild("farmers").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        val id = databaseReference1.push().key
                        val userData = UserData(
                            id,
                            username,
                            email,
                            mobile,
                            address,
                            district,
                            state,
                            pincode,
                            type,
                            password
                        )
                        databaseReference1.child(id!!).setValue(userData)
                        Toast.makeText(
                            this@RegisterPage, "Register Successfully", Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@RegisterPage, LoginPage::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@RegisterPage, "Already Exists", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseerror: DatabaseError) {
                    Toast.makeText(
                        this@RegisterPage,
                        "Database Error:${databaseerror.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}