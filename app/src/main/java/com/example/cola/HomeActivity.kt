package com.example.cola

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    val userId = Firebase.auth.currentUser.toString()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val BottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)


        BottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home ->
                    loadFragment(HomeFragment())

                R.id.add ->
                    loadFragment(AddFragment())

                R.id.profile ->
                    loadFragment(ProfileFragment())
            }
            true
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    val logout = AlertDialog.Builder(this)
                    logout.setPositiveButton("Yes") { _, _ ->

                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(this, "Successfully logout", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    logout.setNegativeButton("No") { _, _ ->
                    }
                    val alertDialog = logout.create()
                    alertDialog.setMessage("Are you sure want to logout")
                    alertDialog.setTitle("Logout")
                    alertDialog.setIcon(R.drawable.baseline_logout_24)
                    alertDialog.show()

                }

                R.id.delete -> {
                    val alertDialog = AlertDialog.Builder(this)
                    alertDialog.setPositiveButton("Yes") { _, _ ->
                        db.collection("Test").document(userId!!).delete()
                            .addOnSuccessListener {
                                Toast.makeText(this, "Your account is delete", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()

                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Faild", Toast.LENGTH_SHORT).show()
                            }
                    }
                    alertDialog.setNegativeButton("No") { _, _ ->

                    }

                    val dialog = alertDialog.create()
                    dialog.setMessage("Are you sure you want to delete this data")
                    dialog.setTitle("Delete")
                    dialog.setIcon(R.drawable.baseline_delete_24)

                    dialog.show()
                }

                R.id.email -> {
                    val emailSend = Intent(Intent.ACTION_SENDTO)
                    emailSend.data =
                        Uri.parse("mailto:rk34089905@gmail.com") // Replace with the recipient's email address
//                     emailSend.putExtra(Intent.EXTRA_SUBJECT, "Subject of the email") // Optional: add a subject
//                     emailSend.putExtra(Intent.EXTRA_TEXT, "Body of the email") // Optional: add email body
                    startActivity(emailSend)

                }

                R.id.call -> {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    startActivity(callIntent)
                }

            }
            true

        }


    }
    fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
    }


}