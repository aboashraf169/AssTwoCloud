package com.example.asscloudtwo

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.asscloudtwo.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.UUID

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var uri : Uri
    val PDFCode : Int = 100
    val storge = Firebase.storage
    val storageRef = storge.reference
    lateinit var namefile : String
    fun progressDialog(){
        progressDialog= ProgressDialog(this)
        progressDialog.setMessage("Loading..")
        progressDialog.setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ChooesPdf.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("pdf/*")
            startActivityForResult(Intent.createChooser(intent,"Select PDF"),PDFCode)
        }


        binding.UploadPdf.setOnClickListener {
            progressDialog()
            progressDialog.show()
             namefile = UUID.randomUUID().toString()
            storageRef.child("PDF Files/$namefile").putFile(uri)
                .addOnSuccessListener{
                    Toast.makeText(applicationContext,"Upload Succeeded",Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,"Please, Upload Image!",Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
        }


        binding.DownloadPdf.setOnClickListener {
            progressDialog()
            progressDialog.show()

            storageRef.child("PDF Files/$namefile").downloadUrl
                .addOnSuccessListener { uri ->
                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val request = DownloadManager.Request(Uri.parse(uri.toString())).setTitle("$namefile")
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    request.setDestinationInExternalFilesDir(applicationContext,"PDF",".pdf")
                downloadManager.enqueue(request)

                 Toast.makeText(applicationContext,"Download Succeeded",Toast.LENGTH_SHORT).show()
                 progressDialog.dismiss()
            }.addOnFailureListener {
                Toast.makeText(applicationContext,"Please, try again!",Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }

        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == PDFCode) {
                uri = data!!.data!!
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

