package com.example.shoply

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoply.models.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var parentDbName: String = "Users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        listeners()
    }

    private fun listeners() {

        login_btn.setOnClickListener {
            loginUser()
        }
    }

    //  login user from firebase
    private fun loginUser() {

        val phone = loginPhoneNumberInput.text.toString()
        val password = loginPasswordInput.text.toString()

        if (phone.isBlank() || password.isBlank()) {

            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
        } else {

            allowAccessToAccount(phone, password)
        }
    }

    //  Allow user to login into the account
    private fun allowAccessToAccount(phone: String, password: String) {

        val db = FirebaseDatabase.getInstance().reference

        //  Check if the user is in the database or not
        db.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.child(parentDbName).child(phone).exists()) {
                    //  The user exists
                    val usersData: Users? = snapshot.child(parentDbName).child(phone).getValue(Users::class.java)

                    if (usersData!!.phone == phone) {
                        if (usersData.password == password) {
                            Toast.makeText(this@LoginActivity, "Logged in successfully!", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {

                            Toast.makeText(this@LoginActivity, "Incorrect Password", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    //  The user doesn't exist
                    Toast.makeText(applicationContext, "User doesn't exist!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}