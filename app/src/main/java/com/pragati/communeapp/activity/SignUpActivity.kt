package com.pragati.communeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.pragati.communeapp.R


class SignUpActivity : AppCompatActivity() {
    lateinit var btnBack : Button
    lateinit var btnSignUp: Button
    lateinit var username : TextInputLayout
    lateinit var password : TextInputLayout
    lateinit var email : TextInputLayout
    lateinit var phone : TextInputLayout


    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        btnBack= findViewById(R.id.btnBack)
        btnSignUp= findViewById(R.id.btnSignUp)
        username= findViewById(R.id.username)
        password= findViewById<TextInputLayout>(R.id.password)
        email= findViewById<TextInputLayout>(R.id.email)
        phone = findViewById(R.id.phoneNo)



        btnBack.setOnClickListener {
            val intent = Intent(this@SignUpActivity,
                LoginActivity::class.java)
            startActivity(intent)
        }
        btnSignUp.setOnClickListener{
            signUpUser()
        }
    }

    private fun signUpUser(){

        val em  = email.editText?.text.toString()
        val pass = password.editText?.text.toString()

        if(em.isEmpty()){
            email.error = "Please enter email"
            email.requestFocus()
            return
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if(pass.isEmpty()){
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(em, pass)
            .addOnCompleteListener(this@SignUpActivity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this@SignUpActivity,
                                    LoginActivity::class.java))
                                finish()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "SignUpActivity failed. Try again after sometime",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }

    }
}