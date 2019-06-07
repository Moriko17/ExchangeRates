 package com.example.exchangerates

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import java.time.LocalDate
import java.time.YearMonth
import java.util.*


 class MainActivity : AppCompatActivity() {
     var usd: Float = 65f
     var eur: Float = 73f
     var rub: Float = 1f
     var jpy: Float = 0.6f
     val dailyUrl: String = "https://www.cbr-xml-daily.ru/daily_json.js"
     private val client = OkHttpClient()
     var updatingProc: Boolean = false
     var MIN_DATE: Long = 0
     var MAX_DATE: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.textView)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        MAX_DATE = c.timeInMillis + 1000

        val minCalend = Calendar.getInstance()
        minCalend.set(1993, 1, 1)
        MIN_DATE = minCalend.timeInMillis

        textView.text = "" + day + "." + (month + 1) + "." + year

        editValue.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                calc()
            }
        })
        textView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val dateParse = s.split(".")
                var dayFixed = ""
                if (dateParse[0].toInt() < 10) {
                    dayFixed = "0" + dateParse[0].toString()
                } else {
                    dayFixed = dateParse[0]
                }
                var monthFixed = ""
                if (dateParse[1].toInt() + 1 < 10) {
                    monthFixed = "0" + (dateParse[1]).toString()
                } else {
                    monthFixed = dateParse[1]
                }
                val year = dateParse[2]

                updatingProc = true
                val archUrl: String = "https://www.cbr-xml-daily.ru/archive/$year/$monthFixed/$dayFixed/daily_json.js"
                run(archUrl)

                calc()
            }
        })
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calc()
            }

        }
        spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calc()
            }

        }

        run(dailyUrl)
    }

    fun test1(view: View) {
        val textView: TextView = findViewById(R.id.textView)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            textView.setText("" + dayOfMonth + "." + (monthOfYear + 1) + "." + year)
        }, year, month, day)

        dpd.datePicker.minDate = MIN_DATE
        dpd.datePicker.maxDate = MAX_DATE
        dpd.show()
    }

     fun calc() {

         when(updatingProc) {
             true -> {
                 Handler().postDelayed({
                     calc()
                 }, 100)
             }
             false -> {
                 val inView: EditText = findViewById(R.id.editValue)
                 val outView: TextView = findViewById(R.id.resValue)
                 val inCurSp: Spinner = findViewById(R.id.spinner)
                 val outCurSp: Spinner = findViewById(R.id.spinner2)
                 val inCur: String = inCurSp.selectedItem.toString()
                 val outCur: String = outCurSp.selectedItem.toString()
                 var inCoef: Float = 0f
                 var outCoef: Float = 0f

                 if (inView.text.isNullOrEmpty()) {
                     outView.text = 0.toString()
                     return
                 }

                 when(inCur) {
                     "USD" -> inCoef = usd
                     "EUR" -> inCoef = eur
                     "RUB" -> inCoef = rub
                     "JPY" -> inCoef = jpy
                 }

                 when(outCur) {
                     "USD" -> outCoef = usd
                     "EUR" -> outCoef = eur
                     "RUB" -> outCoef = rub
                     "JPY" -> outCoef = jpy
                 }

                 outView.text = (inView.text.toString().toInt() * inCoef / outCoef).toString()
             }
         }
     }

     fun run(url: String) {
         val request = Request.Builder()
             .url(url)
             .build()

         client.newCall(request).enqueue(object : Callback {
             override fun onFailure(call: Call, e: IOException) {}
             override fun onResponse(call: Call, response: Response) {
//                 println(response.body()?.string())
                 val gson = GsonBuilder().create()
                 println(response.code().toString())
                 if (response.code().toString().equals("404")) {
                     var dataParsed = url.split("/")
                     var year = dataParsed[4]
                     var month = dataParsed[5]
                     var day = dataParsed[6]

                     if (day.toInt() > 1) {
                         if (day.toInt() < 11) {
                             day = "0" + (day.toInt() - 1).toString()
                         } else {
                             day = (day.toInt() - 1).toString()
                         }
                     } else {
                         if (month.toInt() > 1) {
                             val myCalend = Calendar.getInstance()
                             myCalend.set(year.toInt(), month.toInt() - 1, day.toInt())
                             day = myCalend.getActualMaximum(Calendar.DAY_OF_MONTH).toString()
                             if (month.toInt() < 11) {
                                 month = "0" + (month.toInt() - 1).toString()
                             } else {
                                 month = (month.toInt() - 1).toString()
                             }
                         } else {
                             year = (year.toInt() - 1).toString()
                             day = "31"
                             month = "12"
                         }
                     }

                     val archUrl: String = "https://www.cbr-xml-daily.ru/archive/$year/$month/$day/daily_json.js"
                     println(archUrl)
                     run(archUrl)
                     return
                 }
                 val res = gson.fromJson(response.body()?.string(), Rest::class.java)
//                 println(res.Valute.USD.Value)

                 usd = res.Valute.USD.Value.toFloat()
                 if (res.Valute.EUR?.Value.isNullOrEmpty()) {
                     eur = 0f
                 } else {
                     eur = res.Valute.EUR!!.Value!!.toFloat()
                 }
                 jpy = res.Valute.JPY.Value.toFloat() / 100
                 updatingProc = false
             }
         })
     }
}

 class Rest(val Valute: Valute)
 class Valute(val USD: USD, val EUR: EUR?, val JPY: JPY)
 class USD(val Value: String)
 class EUR(val Value: String?)
 class JPY(val Value: String)
