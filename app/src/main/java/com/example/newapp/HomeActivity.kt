package com.example.newapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val signOut= findViewById<Button>(R.id.button)
        val name=findViewById<TextView>(R.id.name)

        name.text= Firebase.auth.currentUser?.displayName
        signOut.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }



    }
}