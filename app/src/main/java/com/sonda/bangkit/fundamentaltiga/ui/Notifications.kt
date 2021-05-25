package com.sonda.bangkit.fundamentaltiga.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import com.sonda.bangkit.fundamentaltiga.AlarmReceiver
import com.sonda.bangkit.fundamentaltiga.R

class Notifications : AppCompatActivity() {
    private lateinit var switchReminder: Switch
    private lateinit var alarm: AlarmReceiver
    private lateinit var sharePref: SharedPreferences

    companion object {
        private const val DAILY = "daily"
        const val CONTEXT = "settings_preferences"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notifications"
        switchReminder = findViewById(R.id.switch_reminder)

        alarm = AlarmReceiver()
        sharePref = getSharedPreferences(CONTEXT, Context.MODE_PRIVATE)

        switch()
        onCheckSwitch()


    }

    private fun onCheckSwitch() {
        switchReminder.setOnCheckedChangeListener { _, checked ->
            if (!checked) {
                alarm.cancelAlarm(this)
            } else {
                alarm.setReminder(this, AlarmReceiver.TYPE, getString(R.string.daily_reminder))
            }
            save(checked)
        }
    }

    private fun save(checked: Boolean) {
        val editor = sharePref.edit()
        editor.putBoolean(DAILY, checked)
        editor.apply()
    }

    private fun switch() {
        switchReminder.isChecked = sharePref.getBoolean(DAILY, false)
    }


}

