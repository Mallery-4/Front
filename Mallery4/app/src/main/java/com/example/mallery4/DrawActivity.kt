package com.example.mallery4

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import kotlinx.android.synthetic.main.activity_draw.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.microedition.khronos.opengles.GL10

class DrawActivity : AppCompatActivity() {

    private lateinit var stickerView: StickerView
    private lateinit var stickerView_save: StickerView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        stickerView=findViewById<StickerView>(R.id.stickerView)

        //아이콘 설정
        val deleteIcon= BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.sticker_ic_close_white_18dp)
            ,BitmapStickerIcon.LEFT_TOP)
        val flipIcon=BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.sticker_ic_flip_white_18dp)
            ,BitmapStickerIcon.RIGHT_BOTOM)
        val scaleIcon=BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.sticker_ic_scale_white_18dp)
            ,BitmapStickerIcon.LEFT_BOTTOM)

        val iconList=listOf(deleteIcon,flipIcon,scaleIcon)

        //아이콘에 이벤트 할당
        deleteIcon.setIconEvent(DeleteIconEvent())
        flipIcon.iconEvent= FlipHorizontallyEvent()
        scaleIcon.setIconEvent(ZoomIconEvent())

        //스티커뷰에 아이콘연결
        stickerView.setIcons(iconList)


        // 뒤로가기 버튼
        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

        // 저장
        val saveText = findViewById<TextView>(R.id.save)
        saveText.setOnClickListener {
            //저장전 스티커 조절 없애야함
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_save)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
            stickerView.removeCurrentSticker()
            saveImage()

            val choice = Intent(this, DecorateActivity::class.java)

            //액티비티 이동
            startActivity(choice)
        }

        // 선택한 사진 가져오기
        val canvas = findViewById<FrameLayout>(R.id.canvas_back)
        if (intent.hasExtra("uri")) {
            val uriString = intent.getStringExtra("uri")
            if (uriString != null) {
                val uri = Uri.parse(uriString)
                val drawable = Drawable.createFromStream(contentResolver.openInputStream(uri), uri.toString())
                canvas.background = drawable
            }
        }

        val customView = findViewById<CustomView>(R.id.customView)
        // 꾸미기 속성들
        val pen = findViewById<ImageView>(R.id.pen)
        val erase = findViewById<ImageView>(R.id.erase)
        val sticker = findViewById<ImageView>(R.id.sticker)
        val pen_color = findViewById<LinearLayout>(R.id.pen_color)
        val sticker_img = findViewById<LinearLayout>(R.id.sticker_img)
        pen_color.visibility = View.GONE //처음에는 안보임
        sticker_img.visibility = View.GONE
        val erase_margin = findViewById<LinearLayout>(R.id.erase_margin)

        pen.setOnClickListener {
            pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen2))
            erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase))
            pen_color.visibility = View.VISIBLE
            erase_margin.visibility = View.GONE
            sticker_img.visibility = View.GONE
        }

        sticker.setOnClickListener {
            pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen))
            erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase))
            sticker_img.visibility = View.VISIBLE
            pen_color.visibility = View.GONE
            erase_margin.visibility = View.GONE
            stickerload()
        }

    }

    fun stickerload(){
        val sticker_heart = findViewById<ImageView>(R.id.sticker_heart)
        sticker_heart.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_heart)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        val sticker_heart2 = findViewById<ImageView>(R.id.sticker_heart2)
        sticker_heart2.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_heart2)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        val sticker_sunglass = findViewById<ImageView>(R.id.sticker_sunglass)
        sticker_sunglass.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_sunglass)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        val sticker_face1 = findViewById<ImageView>(R.id.sticker_face1)
        sticker_face1.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_face1)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        val sticker_face2 = findViewById<ImageView>(R.id.sticker_face2)
        sticker_face2.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_face2)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        val sticker_face3 = findViewById<ImageView>(R.id.sticker_face3)
        sticker_face3.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_face3)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        val sticker_noonsong= findViewById<ImageView>(R.id.sticker_noonsong)
        sticker_noonsong.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.sticker_noonsong)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        val sticker_logo= findViewById<ImageView>(R.id.sticker_logo)
        sticker_logo.setOnClickListener {
            val drawable=ContextCompat.getDrawable(this,R.drawable.logo)
            val drawableSticker= DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }
    }



    private fun saveImage() {
       canvas_back.setDrawingCacheEnabled(true) // 캐쉬허용
       // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
       val screenshot = Bitmap.createBitmap(canvas_back.drawingCache)
       canvas_back.setDrawingCacheEnabled(false) // 캐쉬닫기


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


    fun setRed(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 1
        println(customView.whatColor)
    }

    fun setOrange(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 2
        println(customView.whatColor)
    }

    fun setYellow(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 3
        println(customView.whatColor)
    }

    fun setGreen(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 4
        println(customView.whatColor)
    }

    fun setBlue(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 5
        println(customView.whatColor)
    }

    fun setPurple(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 6
        println(customView.whatColor)
    }


    fun setBlack(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 7
        println(customView.whatColor)
    }

    fun setWhite(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.whatColor = 8
        println(customView.whatColor)
    }

    fun clearPaint(v: View) {
        val customView = findViewById<CustomView>(R.id.customView)
        customView.clearPaint()
        pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen))
        erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase2))
        pen_color.visibility = View.GONE
        erase_margin.visibility = View.VISIBLE
        sticker_img.visibility = View.GONE
    }



}




