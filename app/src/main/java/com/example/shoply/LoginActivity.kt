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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var parentDbName: String = "Users"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //  Paper library
        Paper.init(this)

        listeners()
    }

    private fun listeners() {

        login_btn.setOnClickListener {
            loginUser()
        }

        admin_panel_link.setOnClickListener {

            "Login Admin".also { login_btn.text = it }
            "Admins".also { parentDbName = it }
        }

        not_admin_panel_link.setOnClickListener{

            "Login".also { login_btn.text = it }
            "Users".also { parentDbName = it }
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

        //  Initialize paper library to store the user data in the prevalent
        if (remember_me_checkbox.isChecked) {

            //  Store the phone number and the password in paper library
            Paper.book().write(Prevalent().userPhoneKey, phone)
            Paper.book().write(Prevalent().userPasswordKey, password)
        }

        val db = FirebaseDatabase.getInstance().reference

        //  Check if the user is in the database or not
        db.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.child(parentDbName).child(phone).exists()) {
                    //  The user exists
                    val usersData: Users? = snapshot.child(parentDbName).child(phone).getValue(Users::class.java)

                    if (usersData!!.phone ==   phone) {
                        if (usersData.password == password) {

                            //  Check if the user is an admin or not
                            if (parentDbName == "Admins") {

                                Toast.makeText(this@LoginActivity, "Welcome Admin, you logged in successfully!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@LoginActivity, AdminCategoryActivity::class.java)
                                startActivity(intent)

                            } else if (parentDbName == "Users") {

                                Toast.makeText(this@LoginActivity, "Logged in successfully!", Toast.LENGTH_SHORT).show()

                                //  Save the user to the Prevalent Class
                                Prevalent().currentOnlineUser = usersData

                                val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                                    putExtra("USER_NAME", usersData.name)
                                }
                                startActivity(intent)

                            }
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