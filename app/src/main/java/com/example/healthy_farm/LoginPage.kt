package com.example.healthy_farm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import com.google.firebase.FirebaseApp

class LoginPage : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference1: DatabaseReference
    private var userType: String = ""

    private lateinit var sharedPrefFile: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")
        databaseReference1 = firebaseDatabase.reference.child("farmers")

        var editUsername = findViewById<EditText>(R.id.username)
        var editPassword = findViewById<EditText>(R.id.password)
        var editLogin = findViewById<Button>(R.id.login)
        var textRegister = findViewById<TextView>(R.id.register)

        //get user type
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
                    Toast.makeText(this@LoginPage, "Please Select User", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        //Move to Register page
        textRegister.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }

        //onclick event
        editLogin.setOnClickListener {
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()
            val type = userType

            if (username.isNotEmpty() && password.isNotEmpty()) {
                if (type == "Customer") {
                    loginUser(username, password)
                } else {
                    loginFarmer(username, password)
                }
            } else {
                Toast.makeText(this@LoginPage, "All fields are mandatory", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)

                            if (userData != null && userData.password == password) {

                                sharedPrefFile =
                                    getSharedPreferences("userdata", Context.MODE_PRIVATE)
                                val editor = sharedPrefFile.edit()
                                editor.putString("username", username)
                                editor.putString("password", password)
                                editor.putString("type",userData.type)
                                editor.putBoolean("isUserLoggedIn", true)
                                editor.apply()
                                editor.commit()

                                Toast.makeText(
                                    this@LoginPage,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@LoginPage, MainActivity::class.java))
                                finish()
                                return
                            } else {
                                Toast.makeText(
                                    this@LoginPage,
                                    "Login Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginPage, "Login Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@LoginPage,
                        "Database Error:${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun loginFarmer(username: String, password: String) {
        databaseReference1.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)

                            if (userData != null && userData.password == password) {

                                sharedPrefFile =
                                    getSharedPreferences("userdata", Context.MODE_PRIVATE)
                                val editor = sharedPrefFile.edit()
                                editor.putString("username", username)
                                editor.putString("password", password)
                                editor.putString("type",userData.type)
                                editor.putBoolean("isUserLoggedIn", true)
                                editor.apply()
                                editor.commit()

//                                val userlogin: Boolean = true
//                                val sharedPreferences = getSharedPreferences(
//                                    "Userdata",
//                                    Context.MODE_PRIVATE
//                                )
//                                val editor = sharedPreferences.edit()
//                                editor.apply {
//                                    putString("USERNAME_TAG", username)
//                                    putString("PASSWORD_TAG", password)
//                                    putString("USERLOGIN_TAG", userlogin.toString())
//                                }.apply()

                                Toast.makeText(
                                    this@LoginPage,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this@LoginPage, FarmerHome::class.java))
                                finish()
                                return
                            } else {
                                Toast.makeText(
                                    this@LoginPage,
                                    "Incorrect Username or Password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginPage, "Login Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@LoginPage,
                        "Database Error:${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

//    private fun validationUser(_currentUser: ArrayList<String>) {
//        val oldUser = remember.getRememberDetails()
//        if (oldUser[0] == _currentUser[0]) {
//            if (oldUser[1] == _currentUser[1]) {
//                Toast.makeText(this@LoginPage, "Login Success", Toast.LENGTH_SHORT).show()
//                remember.setLoginState(true)
//                startActivity(Intent(this@LoginPage, MainActivity::class.java))
//            } else {
//                Toast.makeText(this@LoginPage, "Password Incorrect", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            Toast.makeText(this@LoginPage, "User Not Valid", Toast.LENGTH_SHORT).show()
//        }
//    }
}