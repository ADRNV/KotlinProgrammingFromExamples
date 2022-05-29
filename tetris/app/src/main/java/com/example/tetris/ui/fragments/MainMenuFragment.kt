package com.example.tetris.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tetris.GameActivity
import com.example.tetris.R
import com.example.tetris.databinding.FragmentMainMenuBinding
import com.example.tetris.storage.AppPreferences
import com.google.android.material.snackbar.Snackbar

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMainMenuBinding

    private lateinit var preferences:AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        preferences = AppPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainMenuBinding.inflate(inflater)

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

        return binding.root
    }

    private fun onBtnNewGameClick(){
        //Navigation to GameFragment.kt
    }

    private fun onBtnClickResetScore(view: View){
        val preferences = AppPreferences(context = requireContext())

        Snackbar.make(view, getString(R.string.reset_score_notify), Snackbar.LENGTH_LONG)
            .show()

        preferences.clearHighScore()
    }

    private fun onBtnClickExit() = System.exit(0)

    companion object {

        @JvmStatic
        fun newInstance() =
            MainMenuFragment().apply {
                arguments = Bundle()
            }
    }
}