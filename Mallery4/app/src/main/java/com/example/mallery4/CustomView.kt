package com.example.mallery4

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View


class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    /*private var path = Path()

    private var paint = Paint()
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null

    init {
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        Log.d("CustomView", "Initialized")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = Bitmap.createBitmap(if (w > 0) w else 1, if (h > 0) h else 1, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        Log.d("test log", "개발자용 로그")

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
    var startX = -1
    var startY = -1
    var stopX = -1
    var stopY = -1
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("touch log2", "개발자용 로그")
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
        if (bitmap == null) {
            onSizeChanged(width, height, 0, 0)
            Log.d("hi","null")
        }
        val paint = Paint() // paint라는 객체를 생성하고
        paint.style = Paint.Style.STROKE // 채워지지 않는 도형 형성
        // paint.strokeWidth = size.toFloat()

        for (i in myShapes.indices) {
            val shape2 = myShapes[i]
            paint.setStrokeWidth(shape2.size.toFloat())
            // 각 도형별(1번째) 사이즈 가져와서 펜 설정.

            if (shape2.color === 1) {
                paint.color = Color.RED
            } else if (shape2.color === 2) {
                paint.color = Color.BLUE
            } else {
                paint.color = Color.GREEN
            }

            when (shape2.shapeType) {
                LINE ->
                    canvas?.drawLine(
                        shape2.startX.toFloat(),
                        shape2.startY.toFloat(),
                        shape2.stopX.toFloat(),
                        shape2.stopY.toFloat(),
                        paint
                    )

// 피타고라스 정리 이용, 원의 반지름 구하기.
                CIRCLE -> {
                    val radius = Math.sqrt(
                        Math.pow(
                            (shape2.stopX - shape2.startX).toDouble(),
                            2.0
                        ) + Math.pow((shape2.stopY - shape2.startY).toDouble(), 2.0)
                    )
                    // 결과적으로 2좌표의 거리를 산출.

                    canvas?.drawCircle(
                        shape2.startX.toFloat(),
                        shape2.startY.toFloat(),
                        radius.toFloat(),
                        paint
                    )


                }

                SQ -> {
                    canvas?.drawRect(
                        shape2.startX.toFloat(),
                        shape2.startY.toFloat(),
                        shape2.stopX.toFloat(),
                        shape2.stopY.toFloat(),
                        paint
                    )
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
                canvas?.drawLine(
                    startX.toFloat(),
                    startY.toFloat(),
                    stopX.toFloat(),
                    stopY.toFloat(),
                    paint
                )

            // 피타고라스 정리 이용, 원의 반지름 구하기.
            CIRCLE -> {
                val radius = Math.sqrt(
                    Math.pow(
                        (stopX - startX).toDouble(),
                        2.0
                    ) + Math.pow((stopY - startY).toDouble(), 2.0)
                )
                // 결과적으로 2좌표의 거리를 산출.

                canvas?.drawCircle(startX.toFloat(), startY.toFloat(), radius.toFloat(), paint)


            }

            SQ -> {
                canvas?.drawRect(
                    startX.toFloat(),
                    startY.toFloat(),
                    stopX.toFloat(),
                    stopY.toFloat(),
                    paint
                )
            }


        }
    }


    fun setImageBitmap(bitmap: Bitmap?) {
        if (bitmap == null || canvas == null) { // null check for bitmap and canvas
            return
        }
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvas?.drawBitmap(mutableBitmap, 0f, 0f, paint)
    }


    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float) {
        canvas?.drawLine(x1, y1, x2, y2, paint)
        invalidate()
    }

    fun drawCircle(x: Float, y: Float, radius: Float) {
        canvas?.drawCircle(x, y, radius, paint)
        invalidate()
    }

    fun drawRectangle(left: Float, top: Float, right: Float, bottom: Float) {
        canvas?.drawRect(left, top, right, bottom, paint)
        invalidate()
    }

    fun clearCanvas() {
        path.reset()
        invalidate()
    }

}


/*
class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var path = Path()
    private var paint = Paint()
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null

    init {
        paint.color = Color.RED
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bitmap = Bitmap.createBitmap(if (w > 0) w else 1, if (h > 0) h else 1, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        Log.d("test log", "개발자용 로그")

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                Log.d("toutch log", "개발자용 로그")
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
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, paint)
        //canvas?.drawBitmap(bitmap!!, 0f, 0f, null)
        Log.d("test log2", "개발자용 로그")
    }
    fun setImageBitmap(bitmap: Bitmap?) {
        if (bitmap == null) {
            return
        }
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvas?.drawBitmap(mutableBitmap, 0f, 0f, paint)
    }
    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float) {
        canvas?.drawLine(x1, y1, x2, y2, paint)
        invalidate()
    }

    fun drawCircle(x: Float, y: Float, radius: Float) {
        canvas?.drawCircle(x, y, radius, paint)
        invalidate()
    }

    fun drawRectangle(left: Float, top: Float, right: Float, bottom: Float) {
        canvas?.drawRect(left, top, right, bottom, paint)
        invalidate()
    }

    fun clearCanvas() {
       invalidate()
    }*/
}



     */


    private val p1 = Paint()
    private val p2 = Paint()
    private val p3 = Paint()
    private val p4 = Paint()
    private val p5 = Paint()


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
        p1.color = Color.RED
        p2.color = Color.BLUE
        p3.color = Color.YELLOW
        p4.color = Color.GREEN
        p5.color = Color.BLACK

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





