package com.example.homework2

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import java.lang.IndexOutOfBoundsException
import kotlin.math.max

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var toppingCost = 0.0
    private var spicyCost = 0.0
    private var sizeCost = 0.0
    private var deliveryCost = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //size spinner
        val sizes = listOf("Choose Pizza Size", "Medium ($9.99)", "Large ($13.99)", "Extra Large ($15.99)", "Party Size ($25.99)")
        val sizeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sizes)
        val sizeSpinner = findViewById<Spinner>(R.id.spinSize)
        sizeSpinner.adapter = sizeAdapter
        sizeSpinner.onItemSelectedListener = this

        //spicy seekbar
        val spicyLevelText = findViewById<TextView>(R.id.textSpicyLevel)
        findViewById<SeekBar>(R.id.seekBar).setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                spicyLevelText.text = "Spicy Level: ${progress+1}"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


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
        sizeCost = getDollarAmount(selectedSize)
        updateCost();

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun updateCost () {
        val quantity = findViewById<TextView>(R.id.textQuantity).text.toString().toDouble()
        var subTotal = (toppingCost + spicyCost+ sizeCost) * quantity
        //.001 for tolerance so that never get a -0.00 as a cost
        val tolerance = .001
        findViewById<TextView>(R.id.textSubtotal).text = "$" + String.format("%.2f", subTotal + tolerance)
        findViewById<TextView>(R.id.textTax).text = "$" + String.format("%.2f", subTotal*.065 + tolerance)
        findViewById<TextView>(R.id.textTotalPrice).text = "$" + String.format("%.2f", subTotal + subTotal*.065 +deliveryCost + tolerance)
    }


    fun getCheckBoxes(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked
            when (view.id) {
                //https://www.baeldung.com/kotlin/when#:~:text=Kotlin's%20when%20expression%20allows%20us,acts%20as%20an%20OR%20operator.
                R.id.checkBroccoli, R.id.checkMushrooms, R.id.checkOlives, R.id.checkOnion, R.id.checkTomate, R.id.checkSpinach -> {
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

    fun switches (view: View) {

        val spicy = findViewById<Switch>(R.id.switchSpicy)
        val delivery = findViewById<Switch>(R.id.switchDelivery)
        if (spicy.isChecked) {
            spicyCost = 1.0
            spicy.text = "Yes, $1.00"
            findViewById<SeekBar>(R.id.seekBar).visibility = View.VISIBLE
            findViewById<TextView>(R.id.textSpicyLevel).visibility = View.VISIBLE
        } else {
            spicyCost = 0.0
            spicy.text = "No, $0.00"
            val seekBar = findViewById<SeekBar>(R.id.seekBar)
            findViewById<TextView>(R.id.textSpicyLevel).visibility = View.INVISIBLE
            seekBar.progress = 0
            seekBar.visibility = View.INVISIBLE
        }
        if (delivery.isChecked) {
            deliveryCost = 2.0
            delivery.text = "Yes, $2.00"
        } else {
            deliveryCost = 0.0
            delivery.text = "No, $0.00"
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

    fun reset(view: View) {
        toppingCost = 0.0
        //spicyCost = 0.0
        //deliveryCost = 0.0

        findViewById<RadioButton>(R.id.plain).isChecked = true
        findViewById<ImageView>(R.id.imgPizza).setImageResource(R.drawable.plain)
        findViewById<Spinner>(R.id.spinSize).setSelection(0)
        val resetGroup = listOf(R.id.checkBroccoli, R.id.checkMushrooms, R.id.checkOlives, R.id.checkOnion, R.id.checkTomate, R.id.checkSpinach, R.id.switchDelivery, R.id.switchSpicy);
        //for each id in the list
        resetGroup.forEach {id ->
            val i = findViewById<View>(id) //find the associated widget
            if (i is CheckBox) { //reset checkboxes
                i.isChecked = false
                //getCheckBoxes(i) would also reset price if added a max ( 0, toppingCost-getDollarAmount(view.text.toString(), or just reset topping cost
            } else if (i is Switch) {
                i.isChecked = false //reset switches

                switches(i) //call this again so dont have to do visibility and text again
            }

        }
        findViewById<TextView>(R.id.textQuantity).text = 1.toString()
        updateCost()
    }
    //send a string in and return a double value of the first dollar amount encountered in the string, specific for how dollar amount is formatted in the layout
    //https://kotlinandroid.org/kotlin/kotlin-iterate-over-words-in-string/#:~:text=To%20iterate%20over%20words%20in,to%20iterate%20over%20the%20words.
    private fun getDollarAmount(string: String): Double {
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