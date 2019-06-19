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
import android.widget.Spinner
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_first.*


/**
 * A simple [Fragment] subclass.
 */
class FirstFragment : Fragment() {

    private lateinit var root: View
    private var usd: Float = 0f
    private var eur: Float = 0f
    private var rub: Float = 1f
    private var jpy: Float = 0f
    private var updatingProc: Boolean = false

    private val dw = DateWorker()
    private val rw = RestWorker()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_first, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            val textView: TextView = root.findViewById(R.id.dateText)

            val currDate = dw.curDateInt()

            val dpd = DatePickerDialog(root.context, DatePickerDialog.OnDateSetListener { _, nYear, nMonth, nDay ->
                textView.text = dw.buildString(nDay, nMonth, nYear)
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
                val outView1: TextView = root.findViewById(R.id.textView2)
                val outView2: TextView = root.findViewById(R.id.textView3)
                val outView3: TextView = root.findViewById(R.id.textView4)
                val inCurSp: Spinner = root.findViewById(R.id.spinnerSolo)

                when(inCurSp.selectedItem.toString()) {
                    "USD" -> {
                        outView1.text = ( usd / eur ).toString()
                        outView2.text = ( usd / rub ).toString()
                        outView3.text = ( usd / jpy ).toString()
                    }
                    "EUR" -> {
                        outView1.text = ( eur / usd ).toString()
                        outView2.text = ( eur / rub ).toString()
                        outView3.text = ( eur / jpy ).toString()
                    }
                    "RUB" -> {
                        outView1.text = ( rub / usd ).toString()
                        outView2.text = ( rub / eur ).toString()
                        outView3.text = ( rub / jpy ).toString()
                    }
                    "JPY" -> {
                        outView1.text = ( jpy / usd ).toString()
                        outView2.text = ( jpy / eur ).toString()
                        outView3.text = ( jpy / rub ).toString()
                    }
                }

                if (outView1.text.toString() == "0.0") {
                    outView1.text = "~~~"
                    outView2.text = "~~~"
                    outView3.text = "~~~"
                }

                if (outView1.text.toString() == "Infinity") {
                    outView1.text = "~~~"
                }

                if (outView2.text.toString() == "Infinity") {
                    outView2.text = "~~~"
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