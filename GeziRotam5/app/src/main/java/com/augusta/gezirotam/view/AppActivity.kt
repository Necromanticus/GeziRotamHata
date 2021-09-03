package com.augusta.gezirotam.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.augusta.gezirotam.databinding.ActivityAppBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AppActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAppBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding.userid.text = "User id: ${userId}"
        binding.emailid.text = "Email id: ${emailId}"
    }

    fun logoutbut(view: View){
        auth.signOut()

        GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            .signOut()

        val intent = Intent(this@AppActivity, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun mapact(view: View){
        val intent = Intent(this@AppActivity, MapsActivity::class.java)
        startActivity(intent)
        finish()
    }

}




