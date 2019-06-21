package com.example.exchangerates

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment : Fragment() {

    private lateinit var root: View

    private var usd: Float = 0f
    private var eur: Float = 0f
    private var rub: Float = 1f
    private var jpy: Float = 0f

    private var updatingProc: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_first, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val dw = DateWorker()
        val rw = RestWorker()

        dateText.text = dw.curDate()

        dateText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val url: String
                updatingProc = true

                val dateList = dw.parseDate(s.toString())
                val currDateList = dw.curDateInt()

                url = if (dateList == currDateList) {
                    rw.urlBuilder()
                } else {
                    rw.urlBuilder(dateList[0], dateList[1], dateList[2])
                }

                rw.requestToCB(url)
                updateRates(rw.getRates())

                calcRates()
            }
        })

        spinnerSolo?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calcRates()
            }
        }

        button1.setOnClickListener {
            val currDate = dw.curDateInt()

            val dpd = DatePickerDialog(root.context, DatePickerDialog.OnDateSetListener { _, nYear, nMonth, nDay ->
                dateText.text = dw.buildString(nDay, nMonth, nYear)
            }, currDate[2], currDate[1], currDate[0])

            dpd.datePicker.minDate = dw.getMin()
            dpd.datePicker.maxDate = dw.getMax()
            dpd.show()
        }

        rw.requestToCB(rw.urlBuilder())
        updateRates(rw.getRates())
    }

    private fun calcRates() {

        when(updatingProc) {
            true -> {
                Handler().postDelayed({
                    calcRates()
                }, 100)
            }
            false -> {
                val iconUSD = R.drawable.ic_usd
                val iconEUR = R.drawable.ic_eur
                val iconRUB = R.drawable.ic_rub
                val iconJPY = R.drawable.ic_jpy

                when(spinnerSolo.selectedItem.toString()) {
                    "USD" -> {
                        textView2.text = ( usd / eur ).toString()
                        textView3.text = ( usd / rub ).toString()
                        textView4.text = ( usd / jpy ).toString()

                        textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconEUR, 0)
                        textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconRUB, 0)
                        textView4.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconJPY, 0)
                    }
                    "EUR" -> {
                        textView2.text = ( eur / usd ).toString()
                        textView3.text = ( eur / rub ).toString()
                        textView4.text = ( eur / jpy ).toString()

                        textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconUSD, 0)
                        textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconRUB, 0)
                        textView4.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconJPY, 0)
                    }
                    "RUB" -> {
                        textView2.text = ( rub / usd ).toString()
                        textView3.text = ( rub / eur ).toString()
                        textView4.text = ( rub / jpy ).toString()

                        textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconUSD, 0)
                        textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconEUR, 0)
                        textView4.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconJPY, 0)
                    }
                    "JPY" -> {
                        textView2.text = ( jpy / usd ).toString()
                        textView3.text = ( jpy / eur ).toString()
                        textView4.text = ( jpy / rub ).toString()

                        textView2.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconUSD, 0)
                        textView3.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconEUR, 0)
                        textView4.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconRUB, 0)
                    }
                }

                if (textView2.text.toString() == "0.0") {
                    textView2.text = "~~~"
                    textView3.text = "~~~"
                    textView4.text = "~~~"
                }

                if (textView2.text.toString() == "Infinity") {
                    textView2.text = "~~~"
                }

                if (textView3.text.toString() == "Infinity") {
                    textView3.text = "~~~"
                }
            }
        }
    }

    fun updateRates(ratesList: MutableList<Float>) {
        usd = ratesList[0]
        eur = ratesList[1]
        jpy = ratesList[2]
        updatingProc = false
    }
}