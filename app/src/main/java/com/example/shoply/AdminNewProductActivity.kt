package com.example.shoply

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_admin_new_product.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class AdminNewProductActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private lateinit var saveCurrentDate: String
    private lateinit var saveCurrentTime: String
    private lateinit var productRandomKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_new_product)

        val productTitleExtra = intent.getStringExtra("CAT_TITLE_EXTRA")
        val productImageExtra: Int = intent.getIntExtra("CAT_IMAGE_EXTRA", R.drawable.tshirts)

        listeners()
    }

    private fun listeners() {

        select_product_image.setOnClickListener {
            openGallery()
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

        val prodName = product_name.text.toString()
        val prodDesc = product_description.text.toString()
        val prodPrice = product_price.text.toString()

        if (imageUri == null) {
            Toast.makeText(this, "Product image is required", Toast.LENGTH_SHORT).show()

        } else if (prodName.isEmpty()) {
            Toast.makeText(this, "Product Name is required", Toast.LENGTH_SHORT).show()

        } else if (prodDesc.isEmpty()) {
            Toast.makeText(this, "Product Description is required", Toast.LENGTH_SHORT).show()

        } else if (prodPrice.isEmpty()) {
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