package com.yugal.firebaseauthdemo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPageActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        tv_email_id.text = "User ID :: $userId"
        tv_user_id.text = "Email ID :: $emailId"

        btn_logout.setOnClickListener(){

            // Logout from app.
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this@MainPageActivity, MainActivity::class.java))
            finish()
        }
    }
}