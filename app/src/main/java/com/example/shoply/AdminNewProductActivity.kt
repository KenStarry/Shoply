package com.example.shoply

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_admin_new_product.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AdminNewProductActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private lateinit var saveCurrentDate: String
    private lateinit var saveCurrentTime: String
    private lateinit var productRandomKey: String
    private lateinit var productName: String
    private lateinit var productDescription: String
    private lateinit var productPrice: String
    private lateinit var productTitleExtra: String
    private var productImageExtra by Delegates.notNull<Int>()
    private lateinit var downloadImageUrl: String

    private lateinit var productImagesRef: StorageReference
    private lateinit var productsRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_new_product)

        productTitleExtra = intent.getStringExtra("CAT_TITLE_EXTRA").toString()
        productImageExtra = intent.getIntExtra("CAT_IMAGE_EXTRA", R.drawable.tshirts)

        productImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
        productsRef = FirebaseDatabase.getInstance().reference.child("Products")

        listeners()
    }

    private fun listeners() {

        select_product_image.setOnClickListener {
            openGallery()
        }

        add_product_button.setOnClickListener {
            validateProducts()
        }
    }

    private fun openGallery() {

        val intent = Intent().apply {

            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }

        getImage.launch(intent)
    }

    //  Function to validate data from edittexts
    private fun validateProducts() {

        productName = product_name.text.toString()
        productDescription = product_description.text.toString()
        productPrice = product_price.text.toString()

        if (imageUri == null) {
            Toast.makeText(this, "Product image is required", Toast.LENGTH_SHORT).show()

        } else if (productName.isEmpty()) {
            Toast.makeText(this, "Product Name is required", Toast.LENGTH_SHORT).show()

        } else if (productDescription.isEmpty()) {
            Toast.makeText(this, "Product Description is required", Toast.LENGTH_SHORT).show()

        } else if (productPrice.isEmpty()) {
            Toast.makeText(this, "Product Price is required", Toast.LENGTH_SHORT).show()

        } else {
            //  Store the product information in firebase
            storeProductInformation()
        }


    }

    private fun storeProductInformation() {

        //  Calendar
        val calendar = Calendar.getInstance()

        val currentDate = SimpleDateFormat("mm dd yyyy")
        saveCurrentDate = currentDate.format(calendar.time)

        val currentTime = SimpleDateFormat("HH : mm : ss a")
        saveCurrentTime = currentTime.format(calendar.time)

        //  Create a unique random key innstead of the firebase random key as an id
        //  Combine currentDate and currentTime to form a unique key
        productRandomKey = saveCurrentDate + saveCurrentTime

        //  Storage Reference
        //  Saving and assigning a unique key to the saved image in firebase
        val filePath =
            productImagesRef.child(imageUri!!.lastPathSegment!! + productRandomKey + ".jpg")
        val uploadTask: UploadTask = filePath.putFile(imageUri!!)

        uploadTask.addOnFailureListener { e ->
            Toast.makeText(this, "Error : $e", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener { snapshot ->

            Toast.makeText(this, "Image Uploaded successfully", Toast.LENGTH_SHORT).show()

            //   Get the image url from firebase
            val uriTask: Task<Uri> = uploadTask.continueWithTask { task ->

                //  Here, we get the url but aren't sure if the image was uploaded successfully or not
                downloadImageUrl = filePath.downloadUrl.toString()
                filePath.downloadUrl
            }
        }.addOnCompleteListener { task ->

            if (task.isSuccessful) {
                //  Ensures that we get the url immediately after the process is completed successfully
                downloadImageUrl = task.result.toString()

                Toast.makeText(this, "Got product image Successfully", Toast.LENGTH_SHORT).show()

                //  Now save information to the database
                saveProductInfoToDatabase()
            }
        }
    }

    private fun saveProductInfoToDatabase() {

        val productMap = mapOf(
            "pid" to productRandomKey,
            "date" to saveCurrentDate,
            "time" to saveCurrentTime,
            "description" to productDescription,
            "image" to downloadImageUrl,
            "category" to productTitleExtra,
            "price" to productPrice,
            "name" to productName,
        )

        productsRef.child(productRandomKey).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {

                    Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "Error : ${task.exception}", Toast.LENGTH_SHORT).show()
            }
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

        if (it.resultCode == Activity.RESULT_OK) {
            imageUri = it.data!!.data

            select_product_image.setImageURI(imageUri)
            select_product_image.scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }


}