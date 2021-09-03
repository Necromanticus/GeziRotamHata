package com.augusta.gezirotam.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.augusta.gezirotam.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth




    }

    fun kayitOl(view: View){
        var email = binding.etMail.text.toString()
        var password = binding.etSifre.text.toString()
        var password2 = binding.etSifreTekrar.text.toString()


        if (email.equals("") && password.equals("") && password2.equals("")){
            Toast.makeText(this,"Bilgilerinizi Girmelisiniz!",Toast.LENGTH_LONG).show()
        }else{
            if (!password.equals("") && !password2.equals("")){
                if (password == password2){
                    if (!email.equals("") && !password.equals("")){
                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            val firebaseUser : FirebaseUser = it.user!!

                            auth.currentUser!!.sendEmailVerification().addOnSuccessListener {
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                Toast.makeText(this@RegisterActivity,"Lütfen E-Mail Adresinizi Doğrulayınız!",Toast.LENGTH_LONG).show()

                            }.addOnFailureListener {
                                Toast.makeText(this@RegisterActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                            }


                        }.addOnFailureListener {
                            Toast.makeText(this@RegisterActivity,it.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                    }else{

                        Toast.makeText(this,"Boş Alanları Doldurunuz!", Toast.LENGTH_SHORT).show()


                    }
                }else{
                    Toast.makeText(this,"Şifreler Aynı Değil",Toast.LENGTH_LONG).show()

                }
            }else{
                Toast.makeText(this,"Şifreleri Giriniz!",Toast.LENGTH_LONG).show()
            }
        }



    }

}