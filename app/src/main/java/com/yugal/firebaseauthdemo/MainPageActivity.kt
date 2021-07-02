package com.yugal.firebaseauthdemo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main_page.*
import java.io.IOException

class MainPageActivity : AppCompatActivity() {

    private var mSelectedImageFileUri: Uri? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        btn_select_image.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED){
                // Select image
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 222)
            }else{
                // Request permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    121)
            }
        }

        btn_upload_image.setOnClickListener{
            if(mSelectedImageFileUri != null){
                val imageExtension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(mSelectedImageFileUri!!))

                val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
                    "Image "+ System.currentTimeMillis()+"."+imageExtension
                )
                sRef.putFile(mSelectedImageFileUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {url ->
                                tv_image_upload_success.text = "Your image was uploaded successfully : $url"
                            }.addOnFailureListener{exception ->
                                Toast.makeText(
                                    this,
                                    exception.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e(javaClass.simpleName, exception.message, exception)
                            }
                    }
            }else{
                Toast.makeText(this,"Please select the image to upload",
                Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 121){
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 222)
        }else{
            Toast.makeText(
                this,
                "Oops, you just denied the permission for storage, You can also allow it from settings.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 222){
                if(data != null){
                    try {
                        mSelectedImageFileUri = data?.data
                        image_view.setImageURI(mSelectedImageFileUri)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this@MainPageActivity, "Image selection Failed!",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}