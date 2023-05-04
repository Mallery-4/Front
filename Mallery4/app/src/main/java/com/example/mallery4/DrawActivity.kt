package com.example.mallery4

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.core.view.doOnLayout
import kotlinx.android.synthetic.main.activity_draw.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.microedition.khronos.opengles.GL10

class DrawActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

        val saveText = findViewById<TextView>(R.id.save)
        // Bitmap 객체 생성
        val customView = findViewById<CustomView>(R.id.customView)

        saveText.setOnClickListener {
            saveImage()
        }

        if (intent.hasExtra("uri")) {
            val uriString = intent.getStringExtra("uri")
            if (uriString != null) {
                val uri = Uri.parse(uriString)
                val drawable = Drawable.createFromStream(contentResolver.openInputStream(uri), uri.toString())
                customView.background = drawable
            }
        }

        val pen = findViewById<ImageView>(R.id.pen)
        val erase = findViewById<ImageView>(R.id.erase)
        val sticker = findViewById<ImageView>(R.id.sticker)
        val pen_color = findViewById<LinearLayout>(R.id.pen_color)
        pen_color.visibility = View.GONE //처음에는 안보임
        val erase_margin = findViewById<LinearLayout>(R.id.erase_margin)

        pen.setOnClickListener {
            pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen2))
            erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase))
            pen_color.visibility = View.VISIBLE
            erase_margin.visibility = View.GONE
        }

        sticker.setOnClickListener {
            pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen))
            erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase))
            pen_color.visibility = View.GONE
            erase_margin.visibility = View.GONE
        }

    }

  /*private fun saveImage() {
      // Bitmap 객체 생성
      val bitmap = Bitmap.createBitmap(customView.width, customView.height, Bitmap.Config.ARGB_8888)
      val canvas = Canvas(bitmap)
      customView.draw(canvas)

      // 파일 저장 위치 지정
      val directory = File(Environment.getExternalStorageDirectory().toString() + "/Gallery/Mallery4")
      if (!directory.exists()) {
            directory.mkdirs()
        }
      val file = File(directory, "image.png")



        // 파일 저장
        try {
            file.createNewFile()
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            // 저장 완료 토스트 메시지 출력
            Toast.makeText(this, "저장완료!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "이미지 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }
*/

    private fun saveImage(){
        // 1. 캐쉬(Cache)를 허용시킨다.
// 2. 그림을 Bitmap 으로 저장.
// 3. 캐쉬를 막는다.
        customView.setDrawingCacheEnabled(true) // 캐쉬허용
        // 캐쉬에서 가져온 비트맵을 복사해서 새로운 비트맵(스크린샷) 생성
        val screenshot = Bitmap.createBitmap(customView.drawingCache)
        customView.setDrawingCacheEnabled(false) // 캐쉬닫기

// SDCard(ExternalStorage) : 외부저장공간
// 접근하려면 반드시 AndroidManifest.xml에 권한 설정을 한다.
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)


// 폴더가 있는지 확인 후 없으면 새로 만들어준다.
        if (dir != null) {
            if(!dir.exists()) dir.mkdirs()
        }

        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(File(dir, "my.png"))
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            Toast.makeText(this, "저장 성공", Toast.LENGTH_SHORT).show()
        }  catch (e:Exception) {
            Log.e("photo", "그림저장오류", e)
            Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show()
        } finally {
            fos?.close()
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
    }



}




