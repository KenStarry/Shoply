package com.example.shoply

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.shoply.databinding.ActivityHomeBinding
import com.example.shoply.prevalent.Prevalent
import io.paperdb.Paper
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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