package pl.krutkowski

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

private const val TAG = "MainActivity"
private const val INITIAL_AGE_VALUE = 25
class MainActivity : AppCompatActivity() {

    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText
    private lateinit var seekBarAge: SeekBar
    private lateinit var tvBMIScore: TextView
    private lateinit var tvAge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etWeight = findViewById(R.id.etWeight)
        seekBarAge = findViewById(R.id.seekBarAge)
        etHeight = findViewById(R.id.etHeight)
        tvBMIScore = findViewById(R.id.tvBMIScore)
        tvAge = findViewById(R.id.tvAge)

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
        val weight: String? = etWeight.text.toString()
        val height: String? = etHeight.text.toString()
        val age = seekBarAge.progress
        //compute the BMI
        if(weight != null && weight.isNotEmpty() && height != null && height.isNotEmpty()){
            val weightDouble = weight.toDouble()
            val heightDouble = height.toDouble()
            //TODO check if not eqals 0
            val bmi = weightDouble/(heightDouble*heightDouble)
        //Update the UI
            tvBMIScore.text = bmi.toString()
        }
    }
}