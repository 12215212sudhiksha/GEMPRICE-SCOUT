package com.example.gemscout

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var gemImage: ImageView
    private lateinit var currentPrice: TextView
    private lateinit var priceChange: TextView
    private lateinit var percentageChange: TextView
    private lateinit var description: TextView
    private lateinit var dailyTip: TextView
    private lateinit var saveFavoriteBtn: Button
    private lateinit var gemDetails: TextView
    private lateinit var gemDetails1: TextView
    private lateinit var gemDetails2: TextView
    private lateinit var gemDetails3: TextView
    private lateinit var favoritesHeading: TextView
    private lateinit var removeFavoriteBtn: Button

    private val favoriteGems = mutableSetOf<String>()

    private val gems = listOf(
        Gem("Diamond", R.drawable.img_2, "₹5,00,000", "+₹10,000", "+2%", "Diamonds are the hardest natural material.", "Diamonds are a symbol of eternal love.", "1.00 ct", "Excellent", "D", "VVS1"),
        Gem("Ruby", R.drawable.img_3, "₹3,00,000", "+₹5,000", "+1.7%", "Rubies are known for their deep red color.", "Rubies protected warriors in battle.", "1.20 ct", "Very Good", "Red", "VS1"),
        Gem("Sapphire", R.drawable.img_4, "₹2,50,000", "-₹2,000", "-0.8%", "Sapphires come in various colors.", "Symbol of wisdom and royalty.", "1.10 ct", "Excellent", "Blue", "VVS2"),
        Gem("Emerald", R.drawable.img_5, "₹4,00,000", "+₹7,000", "+1.8%", "Emeralds are vibrant green.", "Cleopatra’s favorite gemstone.", "1.50 ct", "Good", "Green", "VS2"),
        Gem("Topaz", R.drawable.img_6, "₹1,20,000", "+₹1,500", "+1.3%", "Topaz is linked with intellect.", "Birthstone of November.", "1.30 ct", "Excellent", "Yellow", "SI1"),
        Gem("Opal", R.drawable.img_7, "₹90,000", "-₹500", "-0.5%", "Opals have play-of-color.", "Can contain 20% water.", "1.00 ct", "Fair", "Milky", "I1"),
        Gem("Amethyst", R.drawable.img_8, "₹80,000", "+₹1,000", "+1.25%", "Amethysts are calming.", "Used to prevent intoxication.", "1.40 ct", "Very Good", "Purple", "VS1")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        spinner = findViewById(R.id.spinner_selection)
        gemImage = findViewById(R.id.gem_image)
        currentPrice = findViewById(R.id.current_price)
        priceChange = findViewById(R.id.price_change_value)
        percentageChange = findViewById(R.id.percentage_change_value)
        description = findViewById(R.id.gem_description)
        dailyTip = findViewById(R.id.daily_tip)
        saveFavoriteBtn = findViewById(R.id.save_favorite_btn)
        gemDetails = findViewById(R.id.gem_details)
        gemDetails1 = findViewById(R.id.gem_details1)
        gemDetails2 = findViewById(R.id.gem_details2)
        gemDetails3 = findViewById(R.id.gem_details3)
        favoritesHeading = findViewById(R.id.favorites_heading)
        removeFavoriteBtn = findViewById(R.id.remove_favorite_btn)

        favoritesHeading.visibility = View.GONE

        val spinnerItems = mutableListOf("Select a gem")
        spinnerItems.addAll(gems.map { it.name })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        loadFavoriteGems()
        updateFavoritesView()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) return
                val selectedGem = gems[position - 1]
                updateGemUI(selectedGem)

                saveFavoriteBtn.setOnClickListener {
                    if (favoriteGems.contains(selectedGem.name)) {
                        removeFavoriteGem(selectedGem.name)
                        Toast.makeText(this@MainActivity, "${selectedGem.name} removed from favorites!", Toast.LENGTH_SHORT).show()
                    } else {
                        saveFavoriteGem(selectedGem.name)
                        Toast.makeText(this@MainActivity, "${selectedGem.name} added to favorites!", Toast.LENGTH_SHORT).show()
                    }
                    updateFavoritesView()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // 💎 Implementation of removeFavoriteBtn
        removeFavoriteBtn.setOnClickListener {
            val selectedPosition = spinner.selectedItemPosition
            if (selectedPosition > 0) {
                val selectedGem = gems[selectedPosition - 1]
                if (favoriteGems.contains(selectedGem.name)) {
                    removeFavoriteGem(selectedGem.name)
                    Toast.makeText(this@MainActivity, "${selectedGem.name} removed from favorites!", Toast.LENGTH_SHORT).show()
                    updateFavoritesView()
                } else {
                    Toast.makeText(this@MainActivity, "${selectedGem.name} is not in favorites.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Please select a gem first.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateGemUI(gem: Gem) {
        gemImage.setImageResource(gem.imageRes)
        currentPrice.text = gem.price
        priceChange.text = gem.priceChange
        percentageChange.text = gem.percentageChange
        description.text = gem.description
        dailyTip.text = "💎 Did you know? ${gem.funFact}"
        gemDetails.text = "• Carat: ${gem.carat}"
        gemDetails1.text = "• Cut: ${gem.cut}"
        gemDetails2.text = "• Color: ${gem.color}"
        gemDetails3.text = "• Clarity: ${gem.clarity}"
    }

    private fun saveFavoriteGem(gemName: String) {
        favoriteGems.add(gemName)
        val prefs = getSharedPreferences("favorites", MODE_PRIVATE)
        prefs.edit().putStringSet("gem_names", favoriteGems).apply()
    }

    private fun removeFavoriteGem(gemName: String) {
        favoriteGems.remove(gemName)
        val prefs = getSharedPreferences("favorites", MODE_PRIVATE)
        prefs.edit().putStringSet("gem_names", favoriteGems).apply()
    }

    private fun loadFavoriteGems() {
        val prefs = getSharedPreferences("favorites", MODE_PRIVATE)
        val savedNames = prefs.getStringSet("gem_names", setOf()) ?: setOf()
        favoriteGems.clear()
        favoriteGems.addAll(savedNames)
    }

    private fun updateFavoritesView() {
        favoritesHeading.visibility = View.VISIBLE
        if (favoriteGems.isNotEmpty()) {
            val builder = StringBuilder("Your Favorite Gems:\n\n")
            for (gemName in favoriteGems) {
                builder.append("★ $gemName\n")
            }
            favoritesHeading.text = builder.toString()
        } else {
            favoritesHeading.text = "No favorites yet."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(this, Settings::class.java))
                true
            }
            R.id.menu_alerts -> {
                startActivity(Intent(this, Alerts::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    data class Gem(
        val name: String,
        val imageRes: Int,
        val price: String,
        val priceChange: String,
        val percentageChange: String,
        val description: String,
        val funFact: String,
        val carat: String,
        val cut: String,
        val color: String,
        val clarity: String
    )
}
