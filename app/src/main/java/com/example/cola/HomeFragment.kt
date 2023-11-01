package com.example.cola

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private val collection = Firebase.firestore.collection("Test Image")

    val db = FirebaseFirestore.getInstance()
    lateinit var productAdapter: ProductAdapter
    private lateinit var array: ArrayList<DataModel>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val listView = view.findViewById<RecyclerView>(R.id.recyclerView)

        listView.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
        array = arrayListOf()
        getUserList()
        productAdapter = ProductAdapter(array, requireContext())
        listView.adapter = productAdapter
        return view

    }

    @SuppressLint("SuspiciousIndentation")
    fun getUserList() {
//        db.collection.get()("Test Image").get()
        collection.get()
            .addOnSuccessListener {
                Toast.makeText(context, "successfully", Toast.LENGTH_SHORT).show()

                val result = it.toObjects(DataModel::class.java)
                array.addAll(result)

                productAdapter.notifyDataSetChanged()

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
    }
}