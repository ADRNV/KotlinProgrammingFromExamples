package com.example.tetris.storage

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context:Context) {

    private val HIGH_SCORE_KEY = "HIGH_SCORE"

    private val CURRENT_SCORE_KEY = "CURRENT_SCORE"

    var data:SharedPreferences = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    fun saveHighScore(highScore:Int){
        data.edit().putInt(HIGH_SCORE_KEY, highScore).apply()
    }

    fun getHighScore():Int{
        return data.getInt(HIGH_SCORE_KEY, 0)
    }

    fun clearHighScore(){
        data.edit().putInt(HIGH_SCORE_KEY, 0).apply()
    }
}