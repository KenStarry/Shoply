package com.example.shoply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class AdminNewProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_new_product)

        val productTitleExtra = intent.getStringExtra("CAT_TITLE_EXTRA")
        val productImageExtra: Int = intent.getIntExtra("CAT_IMAGE_EXTRA", R.drawable.tshirts)
    }
}