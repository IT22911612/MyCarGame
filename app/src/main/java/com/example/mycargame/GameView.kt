package com.example.mycargame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class GameView(private var context: Context, private var gameTask: GameTask) : View(context) {

    private var myPaint: Paint = Paint()
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myCarPosition = 1 // Start position in the middle lane
    private val otherCars = ArrayList<HashMap<String, Any>>()

    private var viewWidth = 0
    private var viewHeight = 0


    init {
        myPaint.color = Color.GREEN
        myPaint.textSize = 40f
    }

    @SuppressLint("UseCompatLoadingForDrawables", "DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = width
        viewHeight = height

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time += 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10

        val playerCarDrawable = context.resources.getDrawable(R.drawable.yellow, null)
        playerCarDrawable.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - carHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
            viewHeight - 2
        )
        playerCarDrawable.draw(canvas)

        for (i in otherCars.indices) {
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                val carY = time - otherCars[i]["startTime"] as Int

                val otherCarDrawable = context.resources.getDrawable(R.drawable.red, null)
                otherCarDrawable.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                otherCarDrawable.draw(canvas)

                if (otherCars[i]["lane"] as Int == myCarPosition &&
                    carY > viewHeight - 2 - carHeight && carY < viewHeight - 2
                ) {
                    gameTask.closeGame(score)
                    return
                }

                if (carY > viewHeight + carHeight) {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + abs(score / 8)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        canvas.drawText("Score : $score", 80f, 80f, myPaint)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint)
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val x1 = it.x
                    if (x1 < viewWidth / 3) { // If touch is in the left third of the screen
                        if (myCarPosition > 0) {
                            myCarPosition = 0
                        }
                    } else if (x1 > viewWidth * 2 / 3) { // If touch is in the right third of the screen
                        if (myCarPosition < 2) {
                            myCarPosition = 2
                        }
                    } else { // If touch is in the middle third of the screen
                        myCarPosition = 1
                    }
                    invalidate()
                }
            }
        }
        return true
    }
    fun resetScore() {
        score = 0
    }

}
