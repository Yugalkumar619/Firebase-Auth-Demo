package com.yugal.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.et_email
import kotlinx.android.synthetic.main.activity_main.et_password
import kotlinx.android.synthetic.main.activity_register.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_register.setOnClickListener(){
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            login()
        }

        tv_forget_password.setOnClickListener(){
            startActivity(Intent(this@MainActivity, ForgetPasswordActivity::class.java))
        }
    }

    private fun login(){
        when{
            TextUtils.isEmpty(et_email.text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter email.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            TextUtils.isEmpty(et_password.text.toString().trim() {it <= ' '}) -> {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {

                val email: String = et_email.text.toString().trim{ it <= ' '}
                val password: String = et_password.text.toString().trim{ it <= ' '}

                // Create an instance and create a register a user with email and password.
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->

                            // If the registration is successfully done
                            if(task.isSuccessful) {

                                // Firebase registered user

                                Toast.makeText(
                                    this@MainActivity,
                                    "You are logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val intent =
                                    Intent(this@MainActivity, MainPageActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "user_id",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )

                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                // If the registering is not successful then show error message.
                                Toast.makeText(
                                    this@MainActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
            }
        }

    }
}