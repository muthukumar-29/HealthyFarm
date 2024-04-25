package com.example.healthy_farm

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
//import com.google.firebase.database.core.Context
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import android.content.Context
import java.io.File
import java.io.FileOutputStream

class AddProduct : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var storageRef = Firebase.storage
    private lateinit var uri: Uri
    private lateinit var fileName: String
//    private lateinit var userId: String

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        FirebaseApp.initializeApp(this)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("products")

        storageRef = FirebaseStorage.getInstance()

        val editProductName = findViewById<EditText>(R.id.product_name)
        val editQuantity = findViewById<EditText>(R.id.quantity)
        val editPrice = findViewById<EditText>(R.id.price)
        val editProduce = findViewById<EditText>(R.id.produce)
        val editLocation = findViewById<EditText>(R.id.location)
        val editImage = findViewById<ImageView>(R.id.image)
        val editBrowseButton = findViewById<Button>(R.id.upload_image)
        val btnSubmit = findViewById<Button>(R.id.submit)

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                editImage.setImageURI(it)
                uri = it!!
//                saveImageToLocalDrawableFolder(this, it)
            }
        )

        editBrowseButton.setOnClickListener {
            galleryImage.launch("image/*")
        }

        btnSubmit.setOnClickListener {
            fileName = "image_${System.currentTimeMillis()}"
            storageRef.getReference("products").child(fileName.toString())
                .putFile(uri)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            val userId = FirebaseAuth.getInstance().currentUser!!.uid

                            val mapImage = mapOf(
                                "url" to it.toString()
                            )
                            val databaseReference =
                                FirebaseDatabase.getInstance().getReference("products")
                            databaseReference.child(userId).setValue(mapImage)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@AddProduct,
                                        "Image Uploaded Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
//                                    startActivity(Intent(this@AddProduct, FarmerHome::class.java))
                                }
                                .addOnFailureListener { error ->
                                    Toast.makeText(
                                        this@AddProduct,
                                        it.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                }

            sharedPref = getSharedPreferences("userdata", MODE_PRIVATE)

            val farmername = sharedPref.getString("username", "")
            val productname = editProductName.text.toString()
            val quantity = editQuantity.text.toString()
            val price = editPrice.text.toString()
            val produce = editProduce.text.toString()
            val location = editLocation.text.toString()
            val image = fileName

            if (farmername != null && productname.isNotEmpty() && quantity.isNotEmpty() && price.isNotEmpty() && produce.isNotEmpty() && location.isNotEmpty()) {
                addProduct(productname, farmername, quantity, price, produce, location, image)
            } else {
                Toast.makeText(this@AddProduct, "All Fields are Mandatory", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

//    fun saveImageToLocalDrawableFolder(context: Context, imageUri: Uri) {
//        // Get the content resolver
//        val contentResolver = context.contentResolver
//
//        // Open an input stream from the image URI
//        val inputStream = contentResolver.openInputStream(imageUri)
//
//        // Get the file name from the image URI
//        val fileName = "image.jpg"
//
//        // Create an output stream to the local drawable folder
//        val outputStream = FileOutputStream(File(context.filesDir, fileName))
//
//        // Copy the image data from input stream to output stream
//        inputStream?.use { input ->
//            outputStream.use { output ->
//                input.copyTo(output)
//            }
//        }
//
//        // Close the streams
//        inputStream?.close()
//        outputStream.close()
//    }

    private fun addProduct(
        productname: String,
        farmername: String,
        quantity: String,
        price: String,
        produce: String,
        location: String,
        image: String
    ) {
        databaseReference.orderByChild("products").equalTo(productname)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    if (!datasnapshot.exists()) {
                        val id = databaseReference.push().key
                        val productsAdd = ProductsAdd(
                            id,
                            productname,
                            farmername,
                            quantity,
                            price,
                            produce,
                            location,
                            image
                        )
                        databaseReference.child(id!!).setValue(productsAdd)
                        Toast.makeText(
                            this@AddProduct, "Product added Successfully", Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@AddProduct, FarmerHome::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@AddProduct, "Failed to add", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseerror: DatabaseError) {
                    Toast.makeText(
                        this@AddProduct,
                        "Database Error:${databaseerror.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}