package com.example.exchangerates

import android.os.Handler
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class RestWorker {
    private val client = OkHttpClient()
    private val dw = DateWorker()

    private var valuesRates = mutableListOf<Float>()

    private val urlCB = "https://www.cbr-xml-daily.ru"
    private val urlDay = "/daily_json.js"

    fun urlBuilder(): String {

        return urlCB + urlDay
    }

    fun urlBuilder(day: Int, month: Int, year: Int): String {
        val fixedDate = dw.buildDate(day, month)
        val urlArch = "/archive/$year/${fixedDate[1]}/${fixedDate[0]}"

        return urlCB + urlArch + urlDay
    }

    fun requestToCB(url: String) {
        val request = Request.Builder().url(url).build()

        valuesRates = mutableListOf()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            
            override fun onResponse(call: Call, response: Response) {
                val gson = GsonBuilder().create()
                
                if (response.code().toString() == "404") {
                    val dateParsed = dw.parseString(url)
                    val previousDate = dw.downGradeDate(dateParsed[0], dateParsed[1], dateParsed[2])

                    requestToCB(urlBuilder(previousDate[0], previousDate[1], previousDate[2]))

                    return
                }

                val res = gson.fromJson(response.body()?.string(), Rest::class.java)

                valuesRates.add(res.Valute.USD.Value.toFloat())
                if (res.Valute.EUR?.Value.isNullOrEmpty()) {
                    valuesRates.add(0f)
                } else {
                    valuesRates.add(res.Valute.EUR!!.Value!!.toFloat())
                }
                valuesRates.add(res.Valute.JPY.Value.toFloat() / 100)
            }
        })
    }

    fun getRates(): MutableList<Float> {
        while (valuesRates.size != 3){
            Handler().postDelayed({
                getRates()
            }, 100)
        }

        return valuesRates
    }

}

class Rest(val Valute: Valute)
class Valute(val USD: USD, val EUR: EUR?, val JPY: JPY)
class USD(val Value: String)
class EUR(val Value: String?)
class JPY(val Value: String)