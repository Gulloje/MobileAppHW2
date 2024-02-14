package com.example.homework2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.lang.IndexOutOfBoundsException

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //var subTotal = 0.0;
    lateinit var subTotal: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val sizes = listOf("Choose Pizza Size", "Medium ($9.99)", "Large ($13.99)", "Extra Large ($15.99)", "Party Size ($25.99)")
        val sizeAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sizes)
        val sizeSpinner = findViewById<Spinner>(R.id.spinSize)
        sizeSpinner.adapter = sizeAdapter
        sizeSpinner.onItemSelectedListener = this
        subTotal = findViewById(R.id.textSubtotal)

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

        val toAdd = when (position) {
            0 -> getDollarAmount(selectedSize)
            1 -> getDollarAmount(selectedSize)
            2 ->  getDollarAmount(selectedSize)
            3 ->  getDollarAmount(selectedSize)
            else -> getDollarAmount(selectedSize)
        }
        //findViewById<TextView>(R.id.textSubtotal).text = subTotal.toString();
        //findViewById<TextView>(R.id.textTax).text = (subTotal*.0635).toString();
        updateCost(toAdd);

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //not yet needed
    }

    fun updateCost (priceToAdd: Double) {
        var doubleTotal = getDollarAmount(subTotal.text.toString())
        println(doubleTotal)

        doubleTotal += priceToAdd
        println(doubleTotal)
        findViewById<TextView>(R.id.textSubtotal).text = "$${doubleTotal}" //prob need to format correctly

        findViewById<TextView>(R.id.textTax).text = (doubleTotal*.065).toString();
        //maybe put delivery fee switch here prob not thought because need to take in view
        findViewById<TextView>(R.id.textTotalPrice).text = (doubleTotal + doubleTotal*.065).toString()
    }


    //https://stackoverflow.com/questions/10761942/how-to-declare-an-array-of-checkboxes-in-android
    fun switches(view: View) {
        val delivery = findViewById<Switch>(R.id.switchDelivery)
        val spicy = findViewById<Switch>(R.id.switchSpicy)
        var yesText = "Yes, $1.00"
        var noText = "No, $0.00"
        var total = 0.0;
        if (delivery.isChecked) {
            total +=2
            yesText = "Yes, $2.00"
            delivery.text = yesText
        } else {
            total -= 2
            delivery.text = noText
        }
        if (spicy.isChecked){
            total +=1
            spicy.text = yesText
        } else {
            total -= 1
            spicy.text = noText
        }
        updateCost(total)
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