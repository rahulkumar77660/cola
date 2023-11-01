package com.example.cola

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegistationActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registation)


        val name = findViewById<EditText>(R.id.userName)
        val age = findViewById<EditText>(R.id.userAge)
        val email = findViewById<EditText>(R.id.userEmail)
        val address = findViewById<EditText>(R.id.userAddress)
        val register_Btn = findViewById<Button>(R.id.registationBtn)

        register_Btn.setOnClickListener {
            val userId = auth.currentUser?.uid.toString()
            val db = Firebase.firestore.collection("Test")

            val userdata  = DataClass(
                name.text.toString(),
                age.text.toString().toLong(),
                email.text.toString(),
                address.text.toString(),
                userId
            )
            db.document(userId).set(userdata)
                .addOnSuccessListener {
                    startActivity(Intent(this,HomeActivity::class.java))
                    Toast.makeText(this, "Successfully registation", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@RegistationActivity,"Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}