package com.example.gemscout

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Settings : AppCompatActivity() {

    private lateinit var notificationToggle: Switch
    private lateinit var themeSpinner: Spinner
    private lateinit var preferences: SharedPreferences

    private val themes = arrayOf("Light", "Dark", "System Default")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferences = getSharedPreferences("AppSettings", MODE_PRIVATE)

        notificationToggle = findViewById(R.id.notificationToggle)
        themeSpinner = findViewById(R.id.themeSpinner)

        // Setup Theme Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.adapter = adapter

        // Load saved preferences
        val notificationsEnabled = preferences.getBoolean("notifications_enabled", true)
        val savedThemeIndex = preferences.getInt("theme_index", 2)

        notificationToggle.isChecked = notificationsEnabled
        themeSpinner.setSelection(savedThemeIndex)

        // Notification toggle logic
        notificationToggle.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("notifications_enabled", isChecked).apply()
            Toast.makeText(this, if (isChecked) "Notifications Enabled" else "Notifications Disabled", Toast.LENGTH_SHORT).show()
        }

        // Theme change logic
        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                preferences.edit().putInt("theme_index", position).apply()
                when (position) {
                    0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
