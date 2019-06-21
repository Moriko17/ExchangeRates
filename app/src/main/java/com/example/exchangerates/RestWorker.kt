package com.example.exchangerates

import android.os.Handler
import com.example.exchangerates.data.Rest
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class RestWorker {

    private var valuesRates = mutableListOf<Float>()

    private val urlCB = "https://www.cbr-xml-daily.ru"
    private val urlDay = "/daily_json.js"

    fun urlBuilder(): String {

        return urlCB + urlDay
    }

    fun urlBuilder(day: Int, month: Int, year: Int): String {
        val dw = DateWorker()
        val fixedDate = dw.buildDate(day, month)
        val urlArch = "/archive/$year/${fixedDate[1]}/${fixedDate[0]}"

        return urlCB + urlArch + urlDay
    }

    fun requestToCB(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        valuesRates.clear()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            
            override fun onResponse(call: Call, response: Response) {
                val dw = DateWorker()
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