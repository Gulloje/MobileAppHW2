package com.example.homework2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import org.w3c.dom.Text
import java.lang.IndexOutOfBoundsException

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var toppingCost = 0.0
    var spicyCost = 0.0
    var sizeCost = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val sizes = listOf("Choose Pizza Size", "Medium ($9.99)", "Large ($13.99)", "Extra Large ($15.99)", "Party Size ($25.99)")
        val sizeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sizes)
        val sizeSpinner = findViewById<Spinner>(R.id.spinSize)
        sizeSpinner.adapter = sizeAdapter
        sizeSpinner.onItemSelectedListener = this

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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val selectedSize = parent?.getItemAtPosition(position).toString()

        sizeCost = when (position) {
            0 -> getDollarAmount(selectedSize)
            1 -> getDollarAmount(selectedSize)
            2 ->  getDollarAmount(selectedSize)
            3 ->  getDollarAmount(selectedSize)
            else -> getDollarAmount(selectedSize)
        }
        //findViewById<TextView>(R.id.textSubtotal).text = subTotal.toString();
        //findViewById<TextView>(R.id.textTax).text = (subTotal*.0635).toString();
        updateCost();

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //not yet needed
    }

    fun updateCost () {
        val quantity = findViewById<TextView>(R.id.textQuantity).text.toString().toDouble()
        var subTotal = (toppingCost + spicyCost+ sizeCost) * quantity
        findViewById<TextView>(R.id.textSubtotal).text = "$" + String.format("%.2f", subTotal)
        findViewById<TextView>(R.id.textTax).text = "$" + String.format("%.2f", subTotal*.065)
        val deliveryFee = getDollarAmount(findViewById<Switch>(R.id.switchDelivery).text.toString())
        println("delivery fee" + deliveryFee)
        findViewById<TextView>(R.id.textTotalPrice).text = "$" + String.format("%.2f", subTotal + subTotal*.065 )
    }


    //https://stackoverflow.com/questions/10761942/how-to-declare-an-array-of-checkboxes-in-android

    fun getCheckBoxes(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.checkBroccoli, R.id.checkMushrooms, R.id.checkOlives, R.id.checkOnion, R.id.checkTomate, R.id.checkSpinach -> { //https://www.baeldung.com/kotlin/when#:~:text=Kotlin's%20when%20expression%20allows%20us,acts%20as%20an%20OR%20operator.
                    if (checked) {
                        toppingCost += getDollarAmount(view.text.toString())  //https://stackoverflow.com/questions/24117079/get-text-of-selected-checkboxes-android
                        println(toppingCost)
                    } else {
                        toppingCost -= getDollarAmount(view.text.toString())

                    }
                }

            }
        }
        updateCost()
    }

    fun spicyHandler (view: View) {

        val spicy = findViewById<Switch>(R.id.switchSpicy)
        if (spicy.isChecked) {
            spicyCost = 1.0
            spicy.text = "Yes, $1.00"
            findViewById<SeekBar>(R.id.seekBar).visibility = View.VISIBLE
            findViewById<TextView>(R.id.textSpicyLevel).visibility = View.VISIBLE
        } else {
            spicyCost = 0.0
            spicy.text = "No, $0.00"
            findViewById<TextView>(R.id.textSpicyLevel).visibility = View.INVISIBLE
            findViewById<SeekBar>(R.id.seekBar).visibility = View.INVISIBLE
        }
        updateCost()
    }

    fun adjustQuantity(view: View) {
        var quantity = findViewById<TextView>(R.id.textQuantity).text.toString().toInt()
        if (view.id == R.id.btnMinus && quantity > 1) {
            quantity -= 1
        } else if (view.id == R.id.btnPlus){
            quantity += 1
        }
        findViewById<TextView>(R.id.textQuantity).text = "$quantity"
        updateCost()
    }


    //send a string in and return a double value of the first dollar amount encountered in the string
    //https://kotlinandroid.org/kotlin/kotlin-iterate-over-words-in-string/#:~:text=To%20iterate%20over%20words%20in,to%20iterate%20over%20the%20words.
    fun getDollarAmount(string: String): Double {
        try {
            var amount = string.split("$")
            amount = amount[1].split(")");

            //println(amount[0].toDouble());
            return amount[0].toDouble()
        } catch(e: IndexOutOfBoundsException) {
            //Toast.makeText(this, "No Dollar Value Found", Toast.LENGTH_SHORT).show()
            return 0.0
        }



    }

}