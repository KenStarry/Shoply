package com.example.shoply

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        listeners()
    }

    private fun listeners() {

        register_btn.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {

        //  get user details
        val name = registerUsernameInput.text.toString()
        val phone = registerPhoneNumberInput.text.toString()
        val password = registerPasswordInput.text.toString()

        if (name.isBlank() || phone.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()

        } else {

            validatePhoneNumber(name, phone, password)
        }

    }

    private fun validatePhoneNumber(name: String, phone: String, password: String) {

        //  Get reference to the database
        val db = FirebaseDatabase.getInstance().reference

        db.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (!(snapshot.child("Users").child(phone).exists())) {

                    //  The user doesn't exist hence create an account
                    val userData = mapOf(
                        "name" to name,
                        "phone" to phone,
                        "password" to password
                    )

                    db.child("Users").child(phone).updateChildren(userData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                Toast.makeText(applicationContext, "Account created successfully!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(intent)
                            } else {

                                Toast.makeText(applicationContext, "Network Error! Try again later", Toast.LENGTH_SHORT).show()
                            }
                        }

                } else {

                    Toast.makeText(applicationContext, "The number $phone exists", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}