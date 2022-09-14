package com.example.shoply

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoply.adapters.AdminCategoriesAdapter
import kotlinx.android.synthetic.main.activity_admin_category.*

class AdminCategoryActivity : AppCompatActivity() {

    private lateinit var adapter: AdminCategoriesAdapter

    private val categoryTitles = arrayOf(
        "Tshirts", "Sports", "Dresses",
        "Sweaters", "Glasses", "Bags",
        "Hats", "Shoes", "Headphones",
        "Laptops", "Watches", "Phones"
    )

    private val categoryImages = arrayOf(
        R.drawable.tshirts, R.drawable.sports, R.drawable.female_dresses,
        R.drawable.sweather, R.drawable.glasses, R.drawable.purses_bags,
        R.drawable.hats, R.drawable.shoess, R.drawable.headphoness,
        R.drawable.laptops, R.drawable.watches, R.drawable.mobiles
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_category)

        initializeVariables()

        recyclerView()
    }

    private fun initializeVariables() {

        adapter = AdminCategoriesAdapter(this, categoryImages, categoryTitles)

    }

    private fun recyclerView() {

        adminCategoriesRecycler.adapter = adapter
        adminCategoriesRecycler.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

    }
}