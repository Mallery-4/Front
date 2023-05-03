package com.example.mallery4

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_draw.*

class DrawActivity : AppCompatActivity() {
    private var myView: MyPaintView? = null
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)
        myView = MyPaintView(this)

        if(intent.hasExtra("uri")) {
            val uriString = this.intent.getStringExtra("uri")
            if (uriString != null) {
                val uri = Uri.parse(uriString)
                imagecanvas.setImageURI(uri)
            }
        }

        findViewById<LinearLayout>(R.id.imagecanvas).addView(myView)
        findViewById<RadioGroup>(R.id.radioGroup).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.btnRed -> myView?.mPaint?.color = Color.RED
                R.id.btnGreen -> myView?.mPaint?.color = Color.GREEN
                R.id.btnBlue -> myView?.mPaint?.color = Color.BLUE
            }
        }

        findViewById<Button>(R.id.btnTh).setOnClickListener {
            if (count % 2 == 1) {
                btnTh.setText("Thin")
                myView?.mPaint?.strokeWidth = 10f
                count++
            } else {
                btnTh.setText("Thick")
                myView?.mPaint?.strokeWidth = 20f
                count++
            }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            myView?.getBitmap()?.eraseColor(Color.TRANSPARENT)
            myView?.invalidate()
        }
    }

    private class MyPaintView(context: Context?) : View(context) {
        private var mBitmap: Bitmap? = null
        private var mCanvas: Canvas? = null
        private var mPath: Path? = null
        var mPaint: Paint = Paint()

        init {
            mPath = Path()
            mPaint.color = Color.RED
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = 10f
            mPaint.style = Paint.Style.STROKE
        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            mCanvas = Canvas(mBitmap!!)
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawBitmap(mBitmap!!, 0f, 0f, null) //지금까지 그려진 내용
            canvas.drawPath(mPath!!, mPaint) //현재 그리고 있는 내용
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mPath!!.reset()
                    mPath!!.moveTo(x.toFloat(), y.toFloat())
                }
                MotionEvent.ACTION_MOVE -> mPath!!.lineTo(x.toFloat(), y.toFloat())
                MotionEvent.ACTION_UP -> {
                    mPath!!.lineTo(x.toFloat(), y.toFloat())
                    mCanvas!!.drawPath(mPath!!, mPaint) //mBitmap 에 기록
                    mPath!!.reset()
                }
            }
            invalidate()
            return true
        }

        fun getBitmap(): Bitmap? {
            return mBitmap
        }
    }
}
