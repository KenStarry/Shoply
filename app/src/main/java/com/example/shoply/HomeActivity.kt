package com.example.shoply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        logout_btn.setOnClickListener {
            //  Remove all values from the internal android storage
            Paper.book().destroy()

            val i = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(i)
        }
    }
}