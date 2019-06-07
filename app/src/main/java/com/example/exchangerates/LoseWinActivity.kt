package com.example.exchangerates

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import java.util.*

class LoseWinActivity : AppCompatActivity() {
    var MIN_DATE: Long = 0
    var MAX_DATE: Long = 0

    val mAct = MainActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lose_win)

        val textView: TextView = findViewById(R.id.dateView)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        MAX_DATE = c.timeInMillis + 1000

        val minCalend = Calendar.getInstance()
        minCalend.set(1993, 1, 1)
        MIN_DATE = minCalend.timeInMillis

        textView.text = "" + day + "." + (month + 1) + "." + year

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

                mAct.updatingProc = true
                val archUrl: String = "https://www.cbr-xml-daily.ru/archive/$year/$monthFixed/$dayFixed/daily_json.js"
                mAct.run(archUrl)

                mAct.calc()
            }
        })
    }
}
