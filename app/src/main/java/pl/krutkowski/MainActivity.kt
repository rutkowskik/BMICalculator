package pl.krutkowski

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.util.regex.Matcher
import java.util.regex.Pattern

private const val TAG = "MainActivity"
private const val INITIAL_AGE_VALUE = 25
private const val SEEK_BAR_MIN_AGE = 13
private const val SEEK_BAR_MAX_AGE = 99
class MainActivity : AppCompatActivity() {

    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText
    private lateinit var seekBarAge: SeekBar
    private lateinit var tvBMIScore: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvGenderTest: TextView
    private lateinit var tvBMIDescription: TextView
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var maleRadioButton: RadioButton
    private lateinit var genderRadioGroup: RadioGroup

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etWeight = findViewById(R.id.etWeight)
        seekBarAge = findViewById(R.id.seekBarAge)
        etHeight = findViewById(R.id.etHeight)
        tvBMIScore = findViewById(R.id.tvBMIScore)
        tvAge = findViewById(R.id.tvAge)
        tvBMIDescription = findViewById(R.id.tvBMIDescription)
        femaleRadioButton  = findViewById(R.id.radioButtonFemale)
        maleRadioButton = findViewById(R.id.radioButtonMale)
//        tvGenderTest = findViewById(R.id.tvGenderWindow)
        genderRadioGroup = findViewById(R.id.gender_radio_group)

        //seek bar values
        seekBarAge.min = SEEK_BAR_MIN_AGE
        seekBarAge.max = SEEK_BAR_MAX_AGE

        //additional function to set DecimalPaces
        class DecimalDigitsInputFilter( maxDecimalPlaces: Int) : InputFilter {
            private val pattern: Pattern = Pattern.compile(
                "[0-9]"  + "+((\\.[0-9]{0,"
                        + (maxDecimalPlaces - 1) + "})?)||(\\.)?"
            )
            override fun filter(
                p0: CharSequence?, p1: Int, p2: Int, p3: Spanned?, p4: Int, p5: Int
            ): CharSequence? {
                p3?.apply {
                    val matcher: Matcher = pattern.matcher(p3)
                    return if (!matcher.matches()) "" else null
                }
                return null
            }
        }
        //filters on tvWeight
        val maxLength = 5 // max input number
        val inputFilters = arrayOf<InputFilter>(
            InputFilter.LengthFilter(maxLength),
            DecimalDigitsInputFilter(1)
        )
        etWeight.filters = inputFilters

        //defoult values in the app
        seekBarAge.progress = INITIAL_AGE_VALUE
        tvAge.text = "Age: $INITIAL_AGE_VALUE"

        //dynamic set age value
        seekBarAge.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "OnProgressChanged $p1")
                tvAge.text = "Age: $p1"
                computeBMI()

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
        //gender terminally off
//        genderRadioGroup.setOnCheckedChangeListener{ group, checkedId ->
//            when (checkedId){
//                R.id.radioButtonFemale -> {
//                    tvGenderTest.text = "FEMALE"
//                }
//                R.id.radioButtonMale -> {
//                    tvGenderTest.text = "MALE"
//                }
//            }
//        }

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
        updateBMIScoreDescription(bmi)
    }

    private fun updateBMIScoreDescription(bmi : Double) {

        //get age
        val currentAgeVale = seekBarAge.progress
        var description = ""
        if (currentAgeVale <= 65){
            description = when {
                bmi > 10.0 && bmi < 18.5 -> "UNDERWEIGHT"
                bmi >= 18.5 && bmi < 25 -> "NORMAL"
                bmi >= 25 && bmi < 30 -> "OVERWEIGHT"
                bmi >= 30 && bmi < 35 -> "OBESE"
                bmi >= 35 && bmi < 60 -> "EXTREMELY OBESE"

                else -> "OUT OF RANGE"
            }
        }else {
            description = when {
                bmi > 10.0 && bmi < 23 -> "UNDERWEIGHT"
                bmi >= 23 && bmi < 28 -> "NORMAL"
                bmi >= 28 && bmi < 30 -> "OVERWEIGHT"
                bmi >= 30 && bmi < 35 -> "OBESE"
                bmi >= 35 && bmi < 60 -> "EXTREMELY OBESE"

                else -> "OUT OF RANGE"
            }
        }
        tvBMIDescription.text = description

        //set green color if bmi is NORMAL
        if (description == "NORMAL") {
            tvBMIDescription.setTextColor(
                ContextCompat
                    .getColor(applicationContext, R.color.green)
            )
        } else {
            tvBMIDescription.setTextColor(Color.RED)
        }
    }

    fun goToCalorieCounterPage (view: View){
        val intent = Intent(this, CalorieCounter::class.java)
        startActivity(intent)

    }

    //TODO Features
    //     1.Ile należy zrzucić kilogramów aby być w odpowiedniej wadze!!!
    //     2.Do jakiej wagi chesz schudnąć i oblicza zapotrzebowanie albo zamiast tego żeby zawsze do zielonegp doprowadzało
    //     3.Podaje aktywoścj dzienna jaką trzeba robic
    //     5.KONTROLA WAGI NP. WYSKAKUJE POWIDAOMNIE ZEB SIE ZWAŻYĆ
    //     6.dodac kolejna strone a tam wiecej info.
    //     7.Wskazni taki suwak, który pokazuje jak wyglladaz na skali innycg moze pokazc ile kilogramow nie przytyc zeby bylo ok


}