package com.example.tetris.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.tetris.databinding.FragmentGameBinding
import com.example.tetris.domain.game.AppModel
import com.example.tetris.storage.AppPreferences

class GameFragment : Fragment() {

    private lateinit var binding:FragmentGameBinding

    private lateinit var _appPreferences: AppPreferences

    private val _appModel: AppModel = AppModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val context = requireContext()

        binding = FragmentGameBinding.inflate(inflater, container, false)

        _appPreferences = AppPreferences(context)

        _appModel.preferneces  = AppPreferences(context)

        binding.tetrisView.model = _appModel

        binding.tetrisView.viewContext = context

        binding.tetrisView.setOnTouchListener(this::onTetrisViewTouch)

        updateCurrentScore(0)

        binding.tetrisView.setOnScoreChangedListener {
            updateCurrentScore(it)
        }

        binding.btnRestart.setOnClickListener{
            onBtnRestartClick()
        }

        binding.btnRestart.setOnClickListener {
            onBtnRestartClick()
        }

        updateHighScore()

        return binding.root
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
        updateCurrentScore(0)

    }

    private fun updateHighScore(){
        binding.tvHighScore.text = _appPreferences.getHighScore().toString()
    }

    private fun updateCurrentScore(score:Int){
        binding.tvCurrentScore.text = score.toString()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            GameFragment().apply {
                arguments = Bundle()
            }
    }
}