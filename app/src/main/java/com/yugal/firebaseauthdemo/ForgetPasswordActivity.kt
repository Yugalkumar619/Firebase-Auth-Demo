package com.yugal.firebaseauthdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        btn_forgetPassword.setOnClickListener{
            val email: String = et_email_fp.text.toString().trim() { it <= ' '}
            if(email.isEmpty()){
                Toast.makeText(
                    this@ForgetPasswordActivity,
                    "Please enter email address.",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            Toast.makeText(
                                this@ForgetPasswordActivity,
                                "Email sent successfully to reset your password!",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }else{
                            Toast.makeText(
                                this@ForgetPasswordActivity,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }
}