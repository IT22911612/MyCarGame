package com.example.mycargame

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {
    private lateinit var scoreTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var playAgainButton: Button

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        scoreTextView = findViewById(R.id.scoreText)
        highScoreTextView = findViewById(R.id.highScoreText)
        playAgainButton = findViewById(R.id.playAgainButton)

        sharedPreferences = getSharedPreferences("MyCarGame", MODE_PRIVATE)

        val score = intent.getIntExtra("SCORE", 0)
        scoreTextView.text = "Score: $score"

        // Retrieve high score from SharedPreferences
        val highScore = getHighScore()
        highScoreTextView.text = "High Score: $highScore"

        // Check if the current score is higher than the high score and update if necessary
        if (score > highScore) {
            updateHighScore(score)
            highScoreTextView.text = "High Score: $score"
        }

        playAgainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getHighScore(): Int {
        return sharedPreferences.getInt("HighScore", 0)
    }

    private fun updateHighScore(score: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("HighScore", score)
        editor.apply()
    }

}

