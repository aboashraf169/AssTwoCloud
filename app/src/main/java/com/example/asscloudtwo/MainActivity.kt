package com.example.asscloudtwo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.asscloudtwo.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var imagePath : Uri
    val reqCode : Int = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.root
        val storge = Firebase.storage
        val ref = storge.reference
        binding.ChooesGalary.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,reqCode)
        }
        binding.UploadImg.setOnClickListener {
            ref.child("Image/ " + UUID.randomUUID().toString()).putFile(imagePath)
                .addOnSuccessListener{
                Toast.makeText(applicationContext,"Upload Success",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,"please, Upload Image ",Toast.LENGTH_SHORT).show()

                }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == reqCode){
         imagePath = data!!.data!!
            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver,imagePath)
            binding.img.setImageBitmap(bitMap)
        }
    }
}