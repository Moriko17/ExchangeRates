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
import android.widget.*
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {

    private lateinit var root: View

    private var usd: Float = 0f
    private var eur: Float = 0f
    private var rub: Float = 1f
    private var jpy: Float = 0f

    private var updatingProc: Boolean = false

    private val dw = DateWorker()
    private val rw = RestWorker()
    private val bw = BookmarkWorker()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_second, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dateText2.text = dw.curDate()

        editValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                calc()
            }
        })

        dateText2.addTextChangedListener(object : TextWatcher {
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

                calc()
            }
        })

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calc()
            }
        }

        spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calc()
            }
        }

        button2.setOnClickListener {
            val currDate = dw.curDateInt()

            val dpd = DatePickerDialog(root.context, DatePickerDialog.OnDateSetListener { _, nYear, nMonth, nDay ->
                dateText2.text = dw.buildString(nDay, nMonth, nYear)
            }, currDate[2], currDate[1], currDate[0])

            dpd.datePicker.minDate = dw.getMin()
            dpd.datePicker.maxDate = dw.getMax()
            dpd.show()
        }

        buttonMark.setOnClickListener{

            if (editValue.text.toString() != "" && resValue.text.toString() != "~~~") {
                bw.addMark(
                    dateText2.text.toString(),
                    spinner.selectedItem.toString(),
                    editValue.text.toString().toInt(),
                    spinner2.selectedItem.toString(),
                    resValue.text.toString().toFloat()
                )
                Toast.makeText(root.context, "Added to local history.", Toast.LENGTH_SHORT).show()
            }
        }

        rw.requestToCB(rw.urlBuilder())
        updateRates(rw.getRates())
    }

    private fun calc() {

        when(updatingProc) {
            true -> {
                Handler().postDelayed({
                    calc()
                }, 100)
            }
            false -> {
                val inCur: String = spinner.selectedItem.toString()
                val outCur: String = spinner2.selectedItem.toString()
                var inCoef = 0f
                var outCoef = 0f
                var badCheck = false

                if (editValue.text.isNullOrEmpty()) {
                    resValue.text = "~~~"
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

                var output = (editValue.text.toString().toInt() * inCoef / outCoef).toString()


                if (output == "Infinity" || output == "0.0" || output == "0" || output == "NaN") {
                    output = "~~~"
                    badCheck = true
                }

                if (inCur == outCur && !badCheck) {
                    resValue.text = editValue.text
                } else {
                    resValue.text = output
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