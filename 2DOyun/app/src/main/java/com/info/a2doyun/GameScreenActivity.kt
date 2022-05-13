package com.info.a2doyun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.info.a2doyun.databinding.ActivityGameScreenBinding
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.floor


class GameScreenActivity : AppCompatActivity() {

    private lateinit var activityGameScreenBinding : ActivityGameScreenBinding

    //Positions
    private var mainCharacterX = 0.0f
    private var mainCharacterY = 0.0f
    private var squareX = 0.0f
    private var squareY = 0.0f
    private var triangleX = 0.0f
    private var triangleY = 0.0f
    private var circleX = 0.0f
    private var circleY = 0.0f

    //Dimension
    private var screenWidth = 0
    private var screenHeight = 0
    private var mainCharacterWidth = 0
    private var mainCharacterHeight = 0

    //Controls
    private var touchControl = false
    private var startControl = false

    private val timer = Timer()

    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityGameScreenBinding = DataBindingUtil.setContentView(this,R.layout.activity_game_screen)

        activityGameScreenBinding.square.x=-800.0f
        activityGameScreenBinding.square.y=-800.0f
        activityGameScreenBinding.triangle.x=-800.0f
        activityGameScreenBinding.triangle.y=-800.0f
        activityGameScreenBinding.circle.x=-800.0f
        activityGameScreenBinding.circle.y=-800.0f

        activityGameScreenBinding.Cl.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if(startControl){
                    if(p1?.action == MotionEvent.ACTION_DOWN){
                        Log.e("MotionEvent","ACTION_DOWN : Ekrana dokundu")
                        touchControl = true
                    }
                    if(p1?.action == MotionEvent.ACTION_UP){
                        Log.e("MotionEvent","ACTION_UP : Ekranı bıraktı")
                        touchControl = false
                    }
                }else{
                    startControl = true
                    activityGameScreenBinding.textViewStartGame.visibility = View.INVISIBLE

                    mainCharacterX = activityGameScreenBinding.mainCharacher.x
                    mainCharacterY = activityGameScreenBinding.mainCharacher.y

                    mainCharacterWidth = activityGameScreenBinding.mainCharacher.width
                    mainCharacterHeight = activityGameScreenBinding.mainCharacher.height
                    screenHeight = activityGameScreenBinding.Cl.height
                    screenWidth = activityGameScreenBinding.Cl.width

                    timer.schedule(0,20){
                        Handler(Looper.getMainLooper()).post {
                            mainCharacterMove()
                            objectsMove()
                            clashControl()
                        }
                    }
                }
                return true
            }
        })
    }
    fun mainCharacterMove(){

        val mainCharacterSpeed = screenHeight/60.0f

        if(touchControl){
            mainCharacterY -= mainCharacterSpeed
        }else{
            mainCharacterY += mainCharacterSpeed
        }

        if(mainCharacterY<=0.0f){
            mainCharacterY=0.0f
        }
        if(mainCharacterY>=screenHeight-mainCharacterHeight){
            mainCharacterY=(screenHeight-mainCharacterHeight).toFloat()
        }
        activityGameScreenBinding.mainCharacher.y=mainCharacterY
    }

    fun objectsMove(){
        squareX -= screenWidth/44.0f
        triangleX -= screenWidth/54.0f
        circleX -= screenWidth/36.0f

        if(squareX<0.0f){
            squareX=screenWidth+20.0f
            squareY= floor(Math.random()*screenHeight).toFloat()
        }
        activityGameScreenBinding.square.x=squareX
        activityGameScreenBinding.square.y=squareY

        if(triangleX<0.0f){
            triangleX=screenWidth+20.0f
            triangleY= floor(Math.random()*screenHeight).toFloat()
        }
        activityGameScreenBinding.triangle.x=triangleX
        activityGameScreenBinding.triangle.y=triangleY

        if(circleX<0.0f){
            circleX=screenWidth+20.0f
            circleY= floor(Math.random()*screenHeight).toFloat()
        }
        activityGameScreenBinding.circle.x=circleX
        activityGameScreenBinding.circle.y=circleY
    }

    fun clashControl(){

        val circleMidpointX = circleX + activityGameScreenBinding.circle.width/2.0f
        val circleMidpointY = circleY + activityGameScreenBinding.circle.height/2.0f

        if(0.0f <= circleMidpointX && circleMidpointX <= activityGameScreenBinding.mainCharacher.width
            && mainCharacterY <= circleMidpointY && circleMidpointY <= mainCharacterY+mainCharacterHeight){
            score+=20
            circleX = -10.0f
        }

        val triangleMidpointX = triangleX + activityGameScreenBinding.triangle.width/2.0f
        val triangleMidpointY = triangleY + activityGameScreenBinding.triangle.height/2.0f

        if(0.0f <= triangleMidpointX && triangleMidpointX <= activityGameScreenBinding.mainCharacher.width
            && mainCharacterY <= triangleMidpointY && triangleMidpointY <= mainCharacterY+mainCharacterHeight){
            score+=50
            triangleX = -10.0f
        }

        val squareMidpointX = squareX + activityGameScreenBinding.square.width/2.0f
        val squareMidpointY = squareY + activityGameScreenBinding.square.height/2.0f

        if(0.0f <= squareMidpointX && squareMidpointX <= activityGameScreenBinding.mainCharacher.width
            && mainCharacterY <= squareMidpointY && squareMidpointY <= mainCharacterY+mainCharacterHeight){
            squareX = -10.0f
            timer.cancel()

            val intent = Intent(this@GameScreenActivity,ResultScreenActivity::class.java)
            intent.putExtra("score",score)
            startActivity(intent)
            finish()
        }
        activityGameScreenBinding.textViewScore.text = score.toString()
    }
}