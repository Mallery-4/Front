package com.example.mallery4

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
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
        // setContentView(MyGraphicView(this))

        //레이아웃에 그림을 그리기 위해서는 CustomView를 만들어야 한다.
        setContentView(R.layout.activity_draw)

        val customView = findViewById<CustomView>(R.id.customView)
        /*val clearButton = findViewById<Button>(R.id.clear)

        clearButton.setOnClickListener {
            customView.clearCanvas()
        }*/
        if (intent.hasExtra("uri")) {
            val uriString = this.intent.getStringExtra("uri")
            if (uriString != null) {
                val uri = Uri.parse(uriString)
                imagecanvas.setImageURI(uri)
            }
        }



    }
/*
    class CustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

        private val paint = Paint()

        private var path = Path()

        init {
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 10f
        }

        fun clearCanvas() {
            path.reset()
            invalidate()
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(event.x, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(event.x, event.y)
                }
                MotionEvent.ACTION_UP -> {
                    // Do nothing
                }
            }
            invalidate()
            return true
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawPath(path, paint)
        }

    }*/

/*
   class MyGraphicView(context: Context?) : View(context) {

        private val paint = Paint()

        private var path = Path()

        init {
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 10f
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            Log.d("test log", "개발자용 로그")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    path.moveTo(event.x, event.y)
                }
                MotionEvent.ACTION_MOVE -> {
                    path.lineTo(event.x, event.y)
                }
                MotionEvent.ACTION_UP -> {
                    // Do nothing
                }
            }
            invalidate()
            return true
        }

        override fun onDraw(canvas: Canvas) {
            canvas.drawPath(path, paint)
        }
    }

*/

/*

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // 자바코드에서 메뉴를 구성하는 것. 지난 시간에는
        // layout에서 menu 디렉토리를 생성해서 만들었는데 코드로 만들어보자!!
        super.onCreateOptionsMenu(menu)

        menu?.add(0, 1, 0, "선 그리기")
        menu?.add(0, 2, 0, "원 그리기")
        menu?.add(0, 3, 0, "사각형 그리기")

        val sMenu = menu?.addSubMenu("색상변경==>")
        sMenu?.add(0, 4, 0, "빨강색")
        sMenu?.add(0, 5, 0, "파랑색")
        sMenu?.add(0, 6, 0, "초록색")
        sMenu?.add(0, 7, 0, "선 굵게")
        sMenu?.add(0, 8, 0, "선 가늘게")

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            1 -> {
                curShapee = LINE
                return true
            }

            2 -> {
                curShapee = CIRCLE
                return true
            }

            3 -> {
                curShapee = SQ
                return true
            }

            4 -> {
                color = 1
                return true
            }

            5 -> {
                color = 2
                return true
            }

            6 -> {
                color = 3
                return true
            }

            7 -> {
                size += 5
                return true
            }

            8 -> {
                size -= 5
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

    companion object { // 동반 객체, 자바의 static 역할.
        internal val LINE = 1 // 선
        internal val CIRCLE = 2 // 원
        internal val SQ = 3 // 사각형
        internal var curShapee = LINE // curShape = 1
        internal var color = 1 // 색상 빨강 파랑 녹색
        internal var size = 5 // 선 굵기 기본값
        // internal은 자바의 default 역할.
        // 생성된 클라스와 패키지에서 사용 가능.
        // 다른 패키지 불가.

        internal var myShapes : MutableList<MyShape> = ArrayList()
        // 도형들의 데이터 누적.
    }

    private class MyGraphicView(context: Context) : View(context) {
        var startX = -1
        var startY = -1
        var stopX = -1
        var stopY = -1

        @SuppressLint("WrongCall", "ClickableViewAccessibility")
        override fun onTouchEvent(event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> { //touch 시작, 화면에 손가락 올림.
                    startX = event.x.toInt()
                    startY = event.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    // 화면에서 이동할 때, 화면에서 손가락을 띄였을 때.
                    stopX = event.x.toInt()
                    stopY = event.y.toInt()
                    this.invalidate() // 명령 완료, 그리기 호출.
                }

                // move랑 up을 나눠서 처리하는 이유는, 도형의 잔상이 남지 않도록 하기 위해서.

                MotionEvent.ACTION_UP -> {
                    val shape = MyShape() // 도형 데이터 1건을 저장시킬 객체 생성.
                    shape.shapeType = curShapee
                    shape.startX = startX
                    shape.startY = startY
                    shape.stopX = stopX
                    shape.stopY = stopY
                    shape.color = color
                    shape.size = size
                    myShapes.add(shape) // ArrayList에 저장. 도형 누적.

                    this.invalidate()
                }

            }

            return true
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)

            val paint = Paint() // paint라는 객체를 생성하고
            paint.style = Paint.Style.STROKE // 채워지지 않는 도형 형성
            // paint.strokeWidth = size.toFloat()

            for(i in myShapes.indices){
                val shape2 = myShapes[i]
                paint.setStrokeWidth(shape2.size.toFloat())
                // 각 도형별(1번째) 사이즈 가져와서 펜 설정.

                if(shape2.color === 1){
                    paint.color = Color.RED
                }else if(shape2.color === 2){
                    paint.color = Color.BLUE
                } else{
                    paint.color = Color.GREEN
                }

                when (shape2.shapeType) {
                    LINE ->
                        canvas?.drawLine(shape2.startX.toFloat(), shape2.startY.toFloat(), shape2.stopX.toFloat(), shape2.stopY.toFloat(), paint)

// 피타고라스 정리 이용, 원의 반지름 구하기.
                    CIRCLE -> {
                        val radius = Math.sqrt(
                            Math.pow(
                                (shape2.stopX - shape2.startX).toDouble(),
                                2.0) + Math.pow((shape2.stopY - shape2.startY).toDouble(), 2.0)
                        )
                        // 결과적으로 2좌표의 거리를 산출.

                        canvas?.drawCircle(shape2.startX.toFloat(), shape2.startY.toFloat(), radius.toFloat(), paint)


                    }

                    SQ -> {
                        canvas?.drawRect(shape2.startX.toFloat(), shape2.startY.toFloat(), shape2.stopX.toFloat(), shape2.stopY.toFloat(), paint)
                    }


                }

            }

            if (color == 1) {
                paint.color = Color.RED
            } else if (color == 2) {
                paint.color = Color.BLUE
            } else {
                paint.color = Color.GREEN
            }

            when (curShapee) {
                LINE ->
                    canvas?.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)

                // 피타고라스 정리 이용, 원의 반지름 구하기.
                CIRCLE -> {
                    val radius = Math.sqrt(
                        Math.pow(
                            (stopX - startX).toDouble(),
                            2.0) + Math.pow((stopY - startY).toDouble(), 2.0)
                    )
                    // 결과적으로 2좌표의 거리를 산출.

                    canvas?.drawCircle(startX.toFloat(), startY.toFloat(), radius.toFloat(), paint)


                }

                SQ -> {
                    canvas?.drawRect(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
                }


            }
        }

    } // end_MyGraphicView
*/
}



