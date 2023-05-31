package com.example.mallery4

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat


class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val p1 = Paint()
    private val p2 = Paint()
    private val p3 = Paint()
    private val p4 = Paint()
    private val p5 = Paint()
    private val p6 = Paint()
    private val p7 = Paint()
    private val p8 = Paint()


    private val DATA_MAX_SIZE = 30000
    private var myData_x = IntArray(DATA_MAX_SIZE)
    private var myData_y = IntArray(DATA_MAX_SIZE)
    private var myData_color = IntArray(DATA_MAX_SIZE)

    var radius = 20
    var whatColor = 0

    var dataNumber = 0


    private var mx = 0
    private var my = 0

    init {
        p1.color = Color.parseColor("#F44336")
        p2.color = Color.parseColor("#F49136")
        p3.color = Color.parseColor("#FFEE58")
        p4.color = Color.parseColor("#00CC00")
        p5.color = Color.parseColor("#00BEEA")
        p6.color = Color.parseColor("#7F0099")
        p7.color = Color.parseColor("#000000")
        p8.color = Color.parseColor("#ffffff")

        myData_x[0] = 0
        myData_y[0] = 0
        myData_color[0] = 5

    }

    override fun onDraw(canvas: Canvas) {
        for (i in 1..dataNumber) {
            when (myData_color[i]) {
                1 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p1)
                2 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p2)
                3 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p3)
                4 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p4)
                5 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p5)
                6 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p6)
                7 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p7)
                8 -> canvas.drawCircle(myData_x[i].toFloat(), myData_y[i].toFloat(), radius.toFloat(), p8)

            }
        }

        invalidate()
    }

    fun saveData() {
        myData_x[dataNumber] = mx
        myData_y[dataNumber] = my
        myData_color[dataNumber] = whatColor
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mx = event.x.toInt()
        my = event.y.toInt()

        dataNumber++
        saveData()

        return true
    }


    fun clearPaint() {
        myData_x = IntArray(DATA_MAX_SIZE)
        myData_y = IntArray(DATA_MAX_SIZE)
        myData_color = IntArray(DATA_MAX_SIZE)
    }
}





