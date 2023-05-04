package com.example.mallery4

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_draw.*
import javax.microedition.khronos.opengles.GL10

class DrawActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        val customView = findViewById<CustomView>(R.id.customView)


        if (intent.hasExtra("uri")) {
            val uriString = intent.getStringExtra("uri")
            if (uriString != null) {
                val uri = Uri.parse(uriString)
                val drawable = Drawable.createFromStream(contentResolver.openInputStream(uri), uri.toString())
                imagecanvas.background = drawable
            }
        }

        val pen = findViewById<ImageView>(R.id.pen)
        val erase = findViewById<ImageView>(R.id.erase)
        val sticker = findViewById<ImageView>(R.id.sticker)
        val pen_color = findViewById<LinearLayout>(R.id.pen_color)
        pen_color.visibility = View.GONE //처음에는 안보임

        pen.setOnClickListener {
            pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen2))
            erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase))
            pen_color.visibility = View.VISIBLE
        }

       /* erase.setOnClickListener {
            pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen))
            erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase2))
            pen_color.visibility = View.GONE
        }*/

        sticker.setOnClickListener {
            pen.setImageDrawable(resources.getDrawable(R.drawable.draw_pen))
            erase.setImageDrawable(resources.getDrawable(R.drawable.draw_erase))
            pen_color.visibility = View.GONE
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
    }

}




