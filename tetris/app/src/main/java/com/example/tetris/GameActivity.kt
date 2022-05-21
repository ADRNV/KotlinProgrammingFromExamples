package com.example.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.view.MotionEvent
import android.view.View
import com.example.tetris.databinding.ActivityGameBinding
import com.example.tetris.domain.game.AppModel
import com.example.tetris.storage.AppPreferences
import java.util.function.Consumer

//TODO Rebase to fragments
class GameActivity : AppCompatActivity() {

    lateinit var binding: ActivityGameBinding

    private lateinit var _appPreferences:AppPreferences

    private val _appModel:AppModel = AppModel()

    val appPreferences
    get() = _appPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)

        _appPreferences = AppPreferences(this)

        _appModel.preferneces  = AppPreferences(this)

        binding.tetrisView.model = _appModel

        binding.tetrisView.activity = this

        binding.tetrisView.setOnTouchListener(this::onTetrisViewTouch)

        binding.btnRestart.setOnClickListener{
                onBtnRestartClick()
        }

        binding.btnRestart.setOnClickListener {
            onBtnRestartClick()
        }

        updateHighScore()
        updateCurrentScore()
    }

    private fun onTetrisViewTouch(view:View, motionEvent: MotionEvent): Boolean {

        if(_appModel.isGameOver() || _appModel.isGameAwaitingStart()){

            _appModel.startGame()

            binding.tetrisView.setGameCommandWithDelay(AppModel.Motions.DOWN)

        }else if(_appModel.isGameActive()){

            when(resolveTouchDirection(view, motionEvent)){

                0 -> moveTetromino(AppModel.Motions.LEFT)

                1 -> moveTetromino(AppModel.Motions.ROTATE)

                2 -> moveTetromino(AppModel.Motions.DOWN)

                3 -> moveTetromino(AppModel.Motions.RIGHT)

            }

        }
        return true
    }

    private fun resolveTouchDirection(view: View, motionEvent: MotionEvent):Int {

        val x = motionEvent.x / view.width

        val y = motionEvent.y / view.height

        val direction = if (y > x) {
            if (x > 1 - y) 2 else 0 } else {
            if (x > 1 - y) 3 else 1
        }

        return direction
    }

    private fun moveTetromino(motion: AppModel.Motions) {

        if(_appModel.isGameActive()){

            binding.tetrisView.setGameCommand(motion)

        }
    }


    private fun onBtnRestartClick(){

        _appModel.restartGame()

    }

    private fun updateHighScore(){
        binding.tvHighScore.text = _appPreferences.getHighScore().toString()
    }

    private fun updateCurrentScore(){
        binding.tvCurrentScore.text = "0"
    }
}