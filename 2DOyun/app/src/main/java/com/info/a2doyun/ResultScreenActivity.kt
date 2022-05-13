package com.info.a2doyun

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.info.a2doyun.databinding.ActivityResultScreenBinding

class ResultScreenActivity : AppCompatActivity() {

    private lateinit var activityResultScreenBinding : ActivityResultScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultScreenBinding = DataBindingUtil.setContentView(this,R.layout.activity_result_screen)

        val score = intent.getIntExtra("score",0)
        activityResultScreenBinding.textViewFinalScore.text = score.toString()

        val sp = getSharedPreferences("Result",Context.MODE_PRIVATE)
        val highScore = sp.getInt("highScore",0)

        if(score > highScore){
            val editor = sp.edit()
            editor.putInt("highScore",score)
            editor.commit()
            activityResultScreenBinding.textViewHighScore.text = score.toString()

        }else{
            activityResultScreenBinding.textViewHighScore.text = highScore.toString()
        }

        activityResultScreenBinding.buttonTryAgain.setOnClickListener {
            startActivity(Intent(this@ResultScreenActivity,MainActivity::class.java))
        }
    }
}