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
        val goBackBtn = view.findViewById<ImageButton>(R.id.settings_fragment_btn_go_back)

        val pref = activity?.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        val editor = pref?.edit()
//        editor?.apply()

//        switch.isChecked =
//            pref?.getString(UI_MODE, LIGHT) != LIGHT // "выкл" если тема светлая, "вкл" если темная

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
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor?.putString(UI_MODE, LIGHT)
                sunIcon.setBackgroundResource(R.drawable.icon_sun)
                moonIcon.setBackgroundResource(R.drawable.icon_moon_disabled)
            }
            editor?.apply()
        }

        goBackBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onBackPressed() {
        if (parentFragmentManager.backStackEntryCount > 0)
            parentFragmentManager.popBackStack()
        else
            activity?.onBackPressedDispatcher?.onBackPressed()
    }

    private companion object {

        const val UI_MODE = "UI_MODE"
        const val DARK = "DARK"
        const val LIGHT = "LIGHT"
        const val SETTINGS = "SETTINGS"
    }
}