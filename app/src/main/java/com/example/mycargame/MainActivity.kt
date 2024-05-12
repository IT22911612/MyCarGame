package com.example.mycargame

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)


        startBtn.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        mGameView = GameView(this, this)
        mGameView.setBackgroundResource(R.drawable.road)
        rootLayout.addView(mGameView)
        startBtn.visibility = View.GONE

    }

    @SuppressLint("SetTextI18n")
    override fun closeGame(mScore: Int) {
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE

        // Navigate to GameOverActivity and pass the score
        val intent = Intent(this, GameOverActivity::class.java)
        intent.putExtra("SCORE", mScore)
        startActivity(intent)
        //change 1

        mGameView.resetScore()
    }
}
