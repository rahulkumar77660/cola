package com.example.cola

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class SplasActivity : AppCompatActivity() {
    val auth  = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splas)

        Handler().postDelayed(Runnable {
            if (auth.currentUser?.uid!=null) {

                val intent = Intent(this, HomeActivity ::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        },1000)

    }
}