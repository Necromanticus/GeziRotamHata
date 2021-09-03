package com.augusta.gezirotam.view

import androidx.appcompat.app.AppCompatActivity
import android.widget.VideoView
import android.os.Bundle
import android.view.WindowManager
import com.augusta.gezirotam.R
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer.OnCompletionListener
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    var videoView: VideoView? = null
    var button1: Button? = null
    var button2: Button? = null
    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if (currentUser != null){
            val intent = Intent(this, AppActivity::class.java)
            intent.putExtra("user_id", currentUser.uid)
            intent.putExtra("email_id",currentUser.email)
            startActivity(intent)
            finish()
        }

        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        super.onCreate(savedInstanceState)
        requestWindowFeature(1)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_main)
        button1 = findViewById(R.id.nextActivity)
        button2 = findViewById(R.id.nextActivity2)
        videoView = findViewById(R.id.viewVideo)
        imageView = findViewById(R.id.image)
        val path = "android.resource://com.augusta.gezirotam/" + R.raw.coffe
        val uri = Uri.parse(path)
        videoView!!.setVideoURI(uri)
        videoView!!.setOnPreparedListener(OnPreparedListener { mp ->
            mp.start()
            imageView!!.setVisibility(View.GONE)
        })
        videoView!!.setOnCompletionListener(OnCompletionListener { mp -> mp.start() })


        button1!!.setOnClickListener(View.OnClickListener {
            val intent= Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

        })
        button2!!.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    RegisterActivity::class.java
                )
            )
        })
    }
}