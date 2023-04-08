package pl.krutkowski

import android.animation.ArgbEvaluator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_AGE_VALUE = 25
class MainActivity : AppCompatActivity() {

    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText
    private lateinit var seekBarAge: SeekBar
    private lateinit var tvBMIScore: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvBMIDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etWeight = findViewById(R.id.etWeight)
        seekBarAge = findViewById(R.id.seekBarAge)
        etHeight = findViewById(R.id.etHeight)
        tvBMIScore = findViewById(R.id.tvBMIScore)
        tvAge = findViewById(R.id.tvAge)
        tvBMIDescription = findViewById(R.id.tvBMIDescription)

        //defoult values in the app
        seekBarAge.progress = INITIAL_AGE_VALUE
        tvAge.text = "$INITIAL_AGE_VALUE lat"
        //dynamic set age value
        seekBarAge.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "OnProgressChanged $p1")
                tvAge.text = "$p1 lat"

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        //dynamic set weight value
        etWeight.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeBMI()
            }

        })

        //dynamic set height value
        etHeight.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeBMI()
            }

        })

    }
    private fun computeBMI() {
        // Get the value of height and weight
        if(etWeight.text.isEmpty() || etHeight.text.isEmpty()){
            return
        }
        //compute the BMI

         val weight = etWeight.text.toString().toDouble()
         val height = etHeight.text.toString().toDouble()

        val bmi = (weight/(height*height))*10000
       //Update the UI
        tvBMIScore.text = "%.2f".format(bmi)
        updateBMIScoreDescription(bmi.toInt())
    }

    private fun updateBMIScoreDescription(bmi: Int) {
        val description = when (bmi) {
            in 10..18 -> "UNDERWEIGHT"
            in 19..24 -> "NORMAL"
            in 25..30 -> "OVERWEIGHT"
            in 30..34 -> "OBESE"
            in 35..50 -> "EXTREMELY OBESE"
            else -> "OUT OF RANGE"
        }
        tvBMIDescription.text = description

        //set green color if bmi is NORMAL
        if(description == "NORMAL"){
            tvBMIDescription.setTextColor(ContextCompat
                .getColor(applicationContext,  R.color.green))
        }else {
            tvBMIDescription.setTextColor(Color.RED)
        }

        /* 1. TODO Fajnie jak były podany zakres, albo ile trzeba zrzucic zeby miec zielone
        2. gender to trzeba jeszcze zeby sie dało wybrac
        3. do jakiej wagi cgcesz schudnąć i oblicza zapotrzebowanie albo zamiast teog żeby zawsze do zielonegp d
        doprowadzało
        4. podaje aktywoścj dzienna jaką trzeba robic
        5. KONTROLA WAGI NP. WYSKAKUJE POWIDAOMNIE ZEB SIE ZWAŻYĆ`

        dodac kolejna strone a tam wiecej info.
                 wskazni taki suwak, który pokazuje jak wyglladaz na skali innycg
                 moze pokazc ile kilogramow nie przytyc zeby bylo*

                 TODO chat GBT to sprawdzenia/
         */

    }
}