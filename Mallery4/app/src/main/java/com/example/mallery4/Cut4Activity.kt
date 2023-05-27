package com.example.mallery4

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_decorate.*

class Cut4Activity: AppCompatActivity() {
    private val PERMISSION_Album = 101 // 앨범 권한 처리
    private val REQUEST_STORAGE = 102 // 앨범에서 이미지 가져오기
    private val selectedImages = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cut4)

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
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_Album
            )
        }

        // 완료 버튼 클릭 리스너 구현
        val textdone = findViewById<TextView>(R.id.deco_done)
        textdone.setOnClickListener {
            //인텐트 선언 및 정의
            val intent = Intent(this, Make4Activity::class.java)

            //액티비티 이동
            startActivity(intent)
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, REQUEST_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val textView = findViewById<TextView>(R.id.deco_done)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                REQUEST_STORAGE -> {
                    data?.clipData?.let { clipData ->
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            if (selectedImages.size < 4) {
                                selectedImages.add(uri)
                            } else {
                                break
                            }
                        }
                    } ?: data?.data?.let { uri ->
                        selectedImages.add(uri)
                    }

                    if (selectedImages.isNotEmpty()) {
                        val firstImageUri = selectedImages[0]
                        imagePreview.setImageURI(firstImageUri)
                        textView.setTextColor(ContextCompat.getColor(this, R.color.highlightcolor))
                    }

                    textView.setOnClickListener {
                        if (selectedImages.size == 4) {
                            val intent = Intent(this, Make4Activity::class.java)
                            intent.putParcelableArrayListExtra("selectedImages", ArrayList(selectedImages))
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "사진을 4개 선택해주세요.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
        }

    }
}