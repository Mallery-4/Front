package com.example.mallery4

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_decorate.*

class DecorateActivity: AppCompatActivity() {
    private val PERMISSION_Album = 101 // 앨범 권한 처리
    private val REQUEST_STORAGE = 102 // 앨범에서 이미지 가져오기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decorate)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

        // 앨범 버튼 클릭 리스너 구현
        val cameraBtn = findViewById<Button>(R.id.camera_btn)
        cameraBtn.setOnClickListener {
            // Request write external storage permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(WRITE_EXTERNAL_STORAGE),
                PERMISSION_Album
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_Album -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted(requestCode)

                } else {
                    permissionDenied(requestCode)
                }
            }
        }
    }

    private fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERMISSION_Album -> openGallery()
        }
    }

    private fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERMISSION_Album -> Toast.makeText(
                this,
                "저장소 권한을 승인해야 앨범에서 이미지를 불러올 수 있습니다.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        MediaStore.Images.Media.CONTENT_TYPE.also { intent.type = it }
        startActivityForResult(intent, REQUEST_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_STORAGE -> {
                    data?.data?.let { uri ->
                        imagePreview.setImageURI(uri)
                    }
                }
            }
        }
    }
}

