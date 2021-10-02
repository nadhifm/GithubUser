package com.nadhif.githubuser.ui.main.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.nadhif.githubuser.R
import com.nadhif.githubuser.receiver.AlarmReceiver

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var languagePreference: Preference
    private lateinit var reminderSwitchPreference: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        init()
        setPreferences()

        languagePreference.setOnPreferenceClickListener {
            val viewIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(viewIntent)
            true
        }

        reminderSwitchPreference.setOnPreferenceClickListener {
            if (reminderSwitchPreference.isChecked) {
                val time = reminderSwitchPreference.summary.toString()
                alarmReceiver.setDailyReminder(
                    requireContext(),
                    time,
                    "Click to see your favorite github user"
                )
            } else {
                alarmReceiver.cancelAlarm(requireContext())
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "alarm") {
            reminderSwitchPreference.isChecked = sharedPreferences.getBoolean("alarm", false)
        }
    }

    private fun init() {
        alarmReceiver = AlarmReceiver()
        reminderSwitchPreference = findPreference<SwitchPreference>("alarm") as SwitchPreference
        languagePreference = findPreference<Preference>("language") as Preference
    }

    private fun setPreferences() {
        val sh = preferenceManager.sharedPreferences
        reminderSwitchPreference.isChecked = sh.getBoolean("alarm", false)
    }
}