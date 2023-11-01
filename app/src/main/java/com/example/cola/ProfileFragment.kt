package com.example.cola

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    val auth = FirebaseAuth.getInstance()
    lateinit var data: DataClass

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        val nametext = view.findViewById<TextView>(R.id.name)
        val agetext = view.findViewById<TextView>(R.id.age)
        val emailtext = view.findViewById<TextView>(R.id.email)
        val addresstext = view.findViewById<TextView>(R.id.address)


//val image_clearBtn = view.findViewById<ImageView>(R.id.image_clear)


        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid.toString()
        db.collection("Test").document(userId).get()
            .addOnSuccessListener { documents ->
                if (documents.exists()) {

                    data = documents.toObject(DataClass::class.java)!!

                    nametext.text = data?.name.toString()
                    agetext.text = data?.age.toString().toLong().toString()
                    emailtext.text = data?.email.toString()
                    addresstext.text = data?.address.toString()
                }

            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), "Fail", Toast.LENGTH_SHORT).show()
            }

        val update = view.findViewById<Button>(R.id.updataBtn)
        update.setOnClickListener {
            val row = LayoutInflater.from(context).inflate(R.layout.edit_dialog, null, false)
            val editname = row.findViewById<EditText>(R.id.u_name)
            val editemail = row.findViewById<EditText>(R.id.u_email)
            val editage = row.findViewById<EditText>(R.id.u_age)
            val editaddress = row.findViewById<EditText>(R.id.u_address)


            editname.setText(data.name)
            editemail.setText(data.email)
            editage.setText(data.age.toString())
            editaddress.setText(data.address)


            val alertDialog = AlertDialog.Builder(context)
            val alertBulider = alertDialog.create()
            alertBulider.setCancelable(false)
            alertBulider.setView(row)
            alertBulider.show()

            val close =row.findViewById<ImageView>(R.id.image_clear)
            close.setOnClickListener {
                alertBulider.dismiss()
            }
            val save = row.findViewById<Button>(R.id.save)
            save.setOnClickListener {
                val progressDialog = ProgressDialog(requireActivity())
                progressDialog.setTitle("ProgressDialog")
                progressDialog.setMessage("Process genrate a few second")
                progressDialog.show()

                db.collection("Test").document(userId).update(
                    "name", editname.text.toString(),
                    "email", editemail.text.toString(),
                    "age", editage.text.toString().toLong(),
                    "address", editaddress.text.toString()
                )
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(context, "update", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "update failed", Toast.LENGTH_SHORT).show()
                    }
            }

        }
        return view
    }
}