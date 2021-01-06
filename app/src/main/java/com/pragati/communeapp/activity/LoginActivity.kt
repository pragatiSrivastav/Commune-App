package com.pragati.communeapp.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pragati.communeapp.R
import com.pragati.communeapp.util.ConnectionManager

class LoginActivity : AppCompatActivity() {
    lateinit var btnSignUp: Button
    lateinit var btnLogin: Button
    lateinit var img: ImageView
    lateinit var logoName: TextView
    lateinit var email: TextInputLayout
    lateinit var password: TextInputLayout
    lateinit var forgetPass: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        //Hooks
        btnSignUp = findViewById(R.id.btnSignUp)
        btnLogin = findViewById(R.id.btnLogin)
        img = findViewById(R.id.logo_image)
        logoName = findViewById(R.id.logo_name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        forgetPass = findViewById(R.id.forgetPass)

        if(!ConnectionManager().checkConnectivity(this@LoginActivity)){
            val dialog = android.app.AlertDialog.Builder(this@LoginActivity)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet connection is not found")
            dialog.setPositiveButton("Open Setting") { text, listener ->
                //do nothing)
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                //do nothing)
                ActivityCompat.finishAffinity(this@LoginActivity)
            }
            dialog.create()
            dialog.show()
        }


        btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            doLogin()
        }

        forgetPass.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
            val username = view.findViewById<EditText>(R.id.et_username)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgetPassword(username)
            })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener { _, _ -> })
            builder.show()
        }
    }

    private fun forgetPassword(username : EditText){
        if(username.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            return
        }
        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val dialog = android.app.AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("Reset your password")
                    dialog.setMessage("Check your mail to reset your password.")
                    dialog.setPositiveButton("Ok") { text, listener ->
                        //do nothing)

                    }
                    dialog.create()
                    dialog.show()
                }else{
                    Toast.makeText(baseContext,"Invalid Email",
                    Toast.LENGTH_SHORT).show()
                }
            }


    }


    private fun doLogin(){
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

        auth.signInWithEmailAndPassword(em, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "LoginActivity failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser : FirebaseUser?){

        if(currentUser != null) {
            if (currentUser.isEmailVerified) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                val dialog = android.app.AlertDialog.Builder(this@LoginActivity)
                dialog.setMessage("Verification email has been sent to your registered mail id.")
                dialog.setPositiveButton("Ok") { text, listener ->
                    //do nothing)
                }
                dialog.create()
                dialog.show()
            }
        }


    }

}