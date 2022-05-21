package com.example.tetris

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tetris.databinding.ActivityMainBinding
import com.example.tetris.storage.AppPreferences
import com.google.android.material.snackbar.Snackbar

//TODO Rebase to fragments
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences:AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)

        preferences = AppPreferences(this)

        binding.tvHighScore.text = getString(R.string.high_score_formatted, preferences.getHighScore())

        binding.btnNewGame.setOnClickListener{
            onBtnNewGameClick()
        }

        binding.btnResetScore.setOnClickListener{
            onBtnClickResetScore(it)
        }

        binding.btnExit.setOnClickListener{
            onBtnClickExit()
        }

        setContentView(binding.root)
    }

    private fun onBtnNewGameClick(){

        val gameIntent = Intent(this,GameActivity::class.java)

        startActivity(gameIntent)
    }

    private fun onBtnClickResetScore(view: View){
        val preferences = AppPreferences(context = this)

        Snackbar.make(view, getString(R.string.reset_score_notify), Snackbar.LENGTH_LONG)
            .show()

        preferences.clearHighScore()
    }

    private fun onBtnClickExit() = System.exit(0)

}