package com.example.cola

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddFragment : Fragment() {

    private val captureImage = 1
    var CAMERA_REQUEST = 100
    var fileUri: Uri? = null
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    lateinit var p_name: EditText
    lateinit var desciption: EditText
    lateinit var price: EditText
    lateinit var product: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        p_name = view.findViewById<EditText>(R.id.p_name)
        desciption = view.findViewById<EditText>(R.id.desciption)
        price = view.findViewById<EditText>(R.id.price)
        product = view.findViewById<ImageView>(R.id.image)

        val image_cam = view.findViewById<ImageView>(R.id.image_cam)
        var galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                product.setImageURI(it)
                if (it != null) {
                    fileUri = it
                }
            }
        )
        image_cam.setOnClickListener {
            val bottomSheet = BottomSheetDialog(requireActivity())
            val layout = layoutInflater.inflate(R.layout.button_cam, null)
            val Gallery = layout.findViewById<ImageView>(R.id.photo)
            val Camera = layout.findViewById<ImageView>(R.id.camera)

            bottomSheet.setContentView(layout)
            bottomSheet.show()
            Gallery.setOnClickListener {
                galleryImage.launch("image/*")
                bottomSheet.show()
                bottomSheet.dismiss()
            }
            Camera.setOnClickListener {
                dispatchTakePicture()
                uploadImage()
                bottomSheet.dismiss()

            }

//            galleryImage.launch("image/*")
        }
        val upload = view.findViewById<Button>(R.id.uplodBtn)
        upload.setOnClickListener {
            uploadImage()
            val productModel = DataModel(
                name = p_name.text.toString(),
                desciption = desciption.text.toString(),
                price = price.text.toString().toLong(),
                userId = auth.currentUser?.uid.toString()
            )
            db.collection("Test Image").document(auth.currentUser?.uid.toString()).set(productModel)
                .addOnSuccessListener {
                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    Toast.makeText(
                        requireActivity(),
                        "suceeesfully upload image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireActivity(), "faile upload", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
    //Uplode Function and update image
    private fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(requireActivity())
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image..")
            progressDialog.show()

            val ref: StorageReference = FirebaseStorage.getInstance().reference
                .child(UUID.randomUUID().toString())
            ref.putFile(fileUri!!).addOnSuccessListener { uri ->

                ref.downloadUrl.addOnSuccessListener {
                    val db = FirebaseFirestore.getInstance()
                    val uid = auth.currentUser?.uid
                    val downloadUrl = it.toString()
                    db.collection("Test Image").document(uid.toString())
                        .update("image", downloadUrl)
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "Updata successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireActivity(), "update Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                progressDialog.dismiss()
                Toast.makeText(requireActivity(), "Image Uploaded..", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(requireActivity(), "Fail to Upload Image..", Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }

    private fun dispatchTakePicture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { a ->
            activity?.packageManager?.let {
                a.resolveActivity(it)?.also {
                    startActivityForResult(a, captureImage)
                }
            } ?: run {
                if (captureImage!=null){
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(resultCode,resultCode,data)

        if(CAMERA_REQUEST == 100){
            fileUri=getImageUri(requireContext(),data?.extras?.get("data") as Bitmap)
            product.setImageURI(fileUri)
        }
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }
}