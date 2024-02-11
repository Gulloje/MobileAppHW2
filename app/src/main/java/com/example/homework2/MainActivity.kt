package com.example.homework2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun pizzaTypeRadio(view: View) {
        val selectedPizzaImg = when (view.id) {
            R.id.bbq -> R.drawable.bbq_chicken
            R.id.margherita -> R.drawable.margheritta //figure out how to spell margherita later
            R.id.hawaiian -> R.drawable.hawaiian
            R.id.pepperoni -> R.drawable.pepperoni
            else -> R.drawable.plain
        }
        findViewById<ImageView>(R.id.imgPizza).setImageResource(selectedPizzaImg);
    }
}