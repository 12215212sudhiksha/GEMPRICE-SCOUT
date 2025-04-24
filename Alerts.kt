package com.example.gemscout

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast

class Alerts : AppCompatActivity() {

    private val alerts = listOf(
        "Diamond price dropped 5%",
        "Emerald price stable for 3 days",
        "Ruby gaining momentum this week",
        "Sapphire price increased by 2%",
        "Topaz demand rising steadily",
        "Opal showing volatile trends",
        "Amethyst holding stable for 5 days"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        val alertsListView = findViewById<ListView>(R.id.alertsListView)
        val switch = findViewById<Switch>(R.id.alertsSwitch)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alerts)
        alertsListView.adapter = adapter

        // Default: alerts enabled
        switch.isChecked = true
        alertsListView.visibility = View.VISIBLE

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                alertsListView.visibility = View.VISIBLE
                Toast.makeText(this, "Price alerts enabled", Toast.LENGTH_SHORT).show()
            } else {
                alertsListView.visibility = View.GONE
                Toast.makeText(this, "Price alerts disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
