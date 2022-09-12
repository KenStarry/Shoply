package com.example.shoply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoply.models.Users
import com.example.shoply.prevalent.Prevalent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Paper.init(this)

        listeners()
    }

    override fun onResume() {
        super.onResume()

        //  Read user data stored in memory
        val userPhoneKey = Paper.book().read<String>(Prevalent().userPhoneKey)
        val userPasswordKey = Paper.book().read<String>(Prevalent().userPasswordKey)

        if (userPhoneKey != null && userPasswordKey != null) {
            if (userPhoneKey.isNotEmpty() && userPasswordKey.isNotEmpty()) {

                allowAccess(userPhoneKey, userPasswordKey)
            }
        }

    }

    private fun listeners() {

        //  Send a user to register activity
        main_join_btn.setOnClickListener {

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        //  Send user to login activity
        main_login_btn.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    //  Allow direct access to the home page
    private fun allowAccess(phone: String, password: String) {

        val db = FirebaseDatabase.getInstance().reference

        db.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.child("Users").child(phone).exists()) {

                    val usersData = snapshot.child("Users").child(phone).getValue(Users::class.java)

                    if (usersData!!.phone == phone) {
                        if (usersData!!.password == password) {

                            Toast.makeText(this@MainActivity, "Welcome Back!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}






