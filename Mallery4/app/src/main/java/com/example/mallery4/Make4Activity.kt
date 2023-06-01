package com.example.mallery4

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_make4.*
import org.chromium.base.Log

class Make4Activity: AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make4)

        // 뒤로가기 버튼
        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

        // 처음에 안보여야 함
        val cut4_frame2 = findViewById<LinearLayout>(R.id.cut4_frame2)
        cut4_frame2.visibility = View.GONE
        val cut4_frame1 = findViewById<LinearLayout>(R.id.cut4_frame1)
        cut4_frame1.visibility = View.VISIBLE

        val title1=findViewById<TextView>(R.id.title1)
        title1.visibility= View.VISIBLE
        val title2=findViewById<TextView>(R.id.title2)
        title2.visibility= View.GONE

        //프레임 선택
        val frame1 = findViewById<ImageView>(R.id.frame1)
        val frame2 = findViewById<ImageView>(R.id.frame2)

        frame1.setOnClickListener {
            frame1.setImageDrawable(resources.getDrawable(R.drawable.frame12))
            frame2.setImageDrawable(resources.getDrawable(R.drawable.frame2))
            cut4_frame1.visibility = View.VISIBLE
            cut4_frame2.visibility = View.GONE
            title1.visibility= View.VISIBLE
            title2.visibility= View.GONE
        }

        frame2.setOnClickListener {
            frame1.setImageDrawable(resources.getDrawable(R.drawable.frame1))
            frame2.setImageDrawable(resources.getDrawable(R.drawable.frame22))
            cut4_frame2.visibility = View.VISIBLE
            cut4_frame1.visibility = View.GONE
            title2.visibility= View.VISIBLE
            title1.visibility= View.GONE
        }


        val saveText = findViewById<TextView>(R.id.save)
        saveText.setOnClickListener {
            //저장전 스티커 조절 없애야함
            val drawable= ContextCompat.getDrawable(this,R.drawable.sticker_save)
            val drawableSticker= DrawableSticker(drawable)
            saveImage()

            val choice = Intent(this, Cut4Activity::class.java)

            //액티비티 이동
            startActivity(choice)
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
                    findViewById<ImageView>(R.id.cut4_4),                    findViewById<ImageView>(R.id.cut4_2_1),
                    findViewById<ImageView>(R.id.cut4_2_2),
                    findViewById<ImageView>(R.id.cut4_2_3),
                    findViewById<ImageView>(R.id.cut4_2_4),
                    findViewById<ImageView>(R.id.cut4_22_1),
                    findViewById<ImageView>(R.id.cut4_22_2),
                    findViewById<ImageView>(R.id.cut4_22_3),
                    findViewById<ImageView>(R.id.cut4_22_4)
                )
                for (i in 0 until imageViews.size) {
                    val uri = selectedImages[i % selectedImages.size] // 순환하여 사진을 가져옴
                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageViews[i].setImageBitmap(bitmap)
                }
            }
        }
    }


        private fun saveImage() {
            cut4.setDrawingCacheEnabled(true) // 캐쉬허용
            // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
            val screenshot = Bitmap.createBitmap(cut4.drawingCache)
            cut4.setDrawingCacheEnabled(false) // 캐쉬닫기


            // 이미지 저장 정보
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "my.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/Mallery4")
                Log.e("접근","접근")
            }

            // 저장소에 이미지 저장
            val contentResolver = applicationContext.contentResolver
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    screenshot.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    Toast.makeText(this, "저장 성공!", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show()
            }

    }
}
