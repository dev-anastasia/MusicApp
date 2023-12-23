package com.example.musicapp.presentation.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.musicapp.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switch = view.findViewById<SwitchCompat>(R.id.theme_switch)
        val sunIcon = view.findViewById<ImageView>(R.id.iv_sun)
        val moonIcon = view.findViewById<ImageView>(R.id.iv_moon)

        val pref = requireActivity().getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val editor = pref?.edit()

        if (pref?.getString(UI_MODE, LIGHT) == LIGHT) {
            moonIcon.setBackgroundResource(R.drawable.icon_moon_disabled)
            switch.isChecked = false
        } else {
            sunIcon.setBackgroundResource(R.drawable.icon_sun_disabled)
            switch.isChecked = true
        }

        switch.setOnCheckedChangeListener { _, _ ->
            if (pref?.getString(UI_MODE, LIGHT) == LIGHT) {
                editor?.putString(UI_MODE, DARK)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                moonIcon.setBackgroundResource(R.drawable.icon_moon)
                sunIcon.setBackgroundResource(R.drawable.icon_sun_disabled)
            } else {
                editor?.putString(UI_MODE, LIGHT)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sunIcon.setBackgroundResource(R.drawable.icon_sun)
                moonIcon.setBackgroundResource(R.drawable.icon_moon_disabled)
            }
            editor?.apply()
        }


    }

    override fun onResume() {
        requireView().findViewById<ImageButton>(R.id.settings_fragment_btn_go_back)
            .setOnClickListener {
                onBackPressed()
            }
        super.onResume()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private companion object {
        const val UI_MODE = "UI_MODE"
        const val DARK = "DARK"
        const val LIGHT = "LIGHT"
        const val SETTINGS = "SETTINGS"
    }
}