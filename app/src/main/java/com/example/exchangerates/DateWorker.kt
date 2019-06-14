package com.example.exchangerates

import java.util.*

class DateWorker {
    private var minDate: Long = 0
    private var maxDate: Long = 0

    private val calendar = Calendar.getInstance()

    init {
        maxDate = calendar.timeInMillis + 1000
        calendar.set(1993, 1, 1)
        minDate = calendar.timeInMillis
    }

    fun getMin(): Long {

        return minDate
    }

    fun getMax(): Long {

        return maxDate
    }

    fun curDate(): String {
        val calendarCurr = Calendar.getInstance()
        val year = calendarCurr.get(Calendar.YEAR)
        val month = calendarCurr.get(Calendar.MONTH) + 1
        val day = calendarCurr.get(Calendar.DAY_OF_MONTH)
        val formatted = buildDate(day, month)

        return "${formatted[0]}/${formatted[1]}/$year"
    }

    fun curDateInt(): MutableList<Int> {
        val calendarCurr = Calendar.getInstance()
        val year = calendarCurr.get(Calendar.YEAR)
        val month = calendarCurr.get(Calendar.MONTH) + 1
        val day = calendarCurr.get(Calendar.DAY_OF_MONTH)
        val dateList = mutableListOf<Int>()

        dateList.add(day)
        dateList.add(month)
        dateList.add(year)

        return dateList
    }

    fun buildDate(day: Int, month: Int): MutableList<String> {
        val formattedDate = mutableListOf<String>()

        val strDay = day.toString()
        if (day < 10) {
            formattedDate.add("0$strDay")
        } else {
            formattedDate.add(strDay)
        }

        val strMonth = month.toString()
        if (month < 10) {
            formattedDate.add("0$strMonth")
        } else {
            formattedDate.add(strMonth)
        }

        return formattedDate
    }

    fun buildString(day: Int, month: Int, year: Int): String {
        val buildedDate = buildDate(day, month + 1)

        return "${buildedDate[0]}/${buildedDate[1]}/$year"
    }

    fun parseString(strToParse: String): MutableList<Int> {
        val parsedList = mutableListOf<Int>()

        val rawData = strToParse.split("/")
        val year = rawData[4]
        val month = rawData[5]
        val day = rawData[6]

        parsedList.add(day.toInt())
        parsedList.add(month.toInt())
        parsedList.add(year.toInt())

        return parsedList
    }

    fun parseDate(strToParse: String): MutableList<Int> {
        val parsedList = mutableListOf<Int>()

        val rawData = strToParse.split("/")
        val year = rawData[2]
        val month = rawData[1]
        val day = rawData[0]

        parsedList.add(day.toInt())
        parsedList.add(month.toInt())
        parsedList.add(year.toInt())

        return parsedList
    }

    fun downGradeDate(day: Int, month: Int, year: Int): MutableList<Int> {
        val prevDate = mutableListOf<Int>()
        val nDay: Int
        var nMonth = month
        var nYear = year

        if (day > 1) {
            nDay = day - 1
        } else {
            if (month > 1) {
                val c = Calendar.getInstance()
                c.set(year, month - 1, day)
                nDay = c.getActualMaximum(Calendar.DAY_OF_MONTH)
                nMonth = month - 1
            } else {
                nYear = year - 1
                nDay = 31
                nMonth = 12
            }
        }

        prevDate.add(nDay)
        prevDate.add(nMonth)
        prevDate.add(nYear)

        return prevDate
    }

}