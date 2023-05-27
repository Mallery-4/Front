package com.example.mallery4

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class Make4Activity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make4)

        // 뒤로가기 버튼
        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

        // 사진 가져오기
        val canvas = findViewById<LinearLayout>(R.id.cut4)
        if (intent.hasExtra("selectedImages")) {
            val selectedImages = intent.getParcelableArrayListExtra<Uri>("selectedImages")
            if (selectedImages != null && selectedImages.size == 4) {
                val imageViews = listOf(
                    findViewById<ImageView>(R.id.cut4_1),
                    findViewById<ImageView>(R.id.cut4_2),
                    findViewById<ImageView>(R.id.cut4_3),
                    findViewById<ImageView>(R.id.cut4_4)
                )
                for (i in 0 until selectedImages.size) {
                    val uri = selectedImages[i]
                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageViews[i].setImageBitmap(bitmap)
                }
            }
        }
    }
}
