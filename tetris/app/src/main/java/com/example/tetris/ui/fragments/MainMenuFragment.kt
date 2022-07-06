package com.example.tetris.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
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
        findNavController()
            .navigate(R.id.action_mainMenuFragment_to_gameFragment,

                null,

                navOptions{
                anim {
                    enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
                    exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                    popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
                    popExit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                }
            })
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