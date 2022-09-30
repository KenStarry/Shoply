package com.example.shoply

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoply.databinding.ActivityHomeBinding
import com.example.shoply.models.Products
import com.example.shoply.viewholder.ProductsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.utilities.Utilities
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var productsRef: DatabaseReference

    private lateinit var recyclerProducts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productsRef = FirebaseDatabase.getInstance().reference.child("Products")
        Paper.init(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBarHome.toolbar.title = "Home"
        setSupportActionBar(binding.appBarHome.toolbar)

        binding.appBarHome.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val toggle: ActionBarDrawerToggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                binding.appBarHome.toolbar,
                R.string.open_drawer,
                R.string.close_drawer
            )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener(this)

        //  Get the header view of the navigation view
        val headerView: View = navView.getHeaderView(0)
        val userName: TextView = headerView.user_profile_name
        val userProfileImage: ImageView = headerView.user_profile_image

        //  Set username from the Prevalent Class
        userName.text = intent.getStringExtra("USER_NAME")

        //  Recycler view for the products displayed in home
        recyclerProducts = findViewById(R.id.recycler_products)
        recyclerProducts.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerProducts.layoutManager = layoutManager
    }


    override fun onStart() {
        super.onStart()

        val options: FirebaseRecyclerOptions<Products> =
            FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productsRef, Products::class.java)
                .build()

        //  Firebase recycler adapter
        val adapter: FirebaseRecyclerAdapter<Products, ProductsViewHolder> = (object : FirebaseRecyclerAdapter<Products, ProductsViewHolder>(options){

            override fun onBindViewHolder(
                holder: ProductsViewHolder,
                position: Int,
                model: Products
            ) {

                holder.prodName.text = model.name.toString()
                holder.prodDesc.text = model.description.toString()
                holder.prodPrice.text = model.price.toString()
                Picasso.get().load(model.image).into(holder.prodImageView)

                Toast.makeText(this@HomeActivity, model.image.toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
                //  Inflating our layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
                return ProductsViewHolder(view)
            }
        })

        recyclerProducts.adapter = adapter
        adapter.startListening()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cart -> {
                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {

                Paper.book().destroy()

                val intent = Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(intent)
            }
        }

        return true
    }
}