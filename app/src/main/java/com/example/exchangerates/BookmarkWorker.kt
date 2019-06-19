package com.example.exchangerates

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*




class BookmarkWorker {
    companion object {
        private var marksList = mutableListOf<Mark>()
    }

    fun addMark(date: String, inCur: String, inValue: Int, outCur: String, outValue: Float) {
        val mark = Mark(date, inCur, inValue, outCur, outValue)

        if (marksList.size == 10) {
            marksList.removeAt(0)
        }
        marksList.add(mark)
    }

    fun saveToJson(context: Context) {

        val jsonString = Gson().toJson(marksList)
        writeToFile(jsonString, context)
    }

    private fun writeToFile(data: String, context: Context) {
        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput("config.json", Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }

    }

    fun readFromFile(context: Context) {

        var ret = ""

        try {
            val inputStream = context.openFileInput("config.json")

            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var receiveString = bufferedReader.readLine()

                while (receiveString != null) {
                    stringBuilder.append(receiveString)
                    receiveString = bufferedReader.readLine()
                }

                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("login activity", "File not found: $e")
            return
        } catch (e: IOException) {
            Log.e("login activity", "Can not read file: $e")
            return
        }

        marksList = parseJSON(ret)

    }

    private fun parseJSON(jsonString: String): MutableList<Mark> {
        val gson = GsonBuilder().create()

        return gson.fromJson(jsonString, Array<Mark>::class.java).toMutableList()
    }

    fun getMarks(): MutableList<Mark> {
        return marksList
    }



}

data class Mark(val date: String, val inCur: String, val inValue: Int, val outCur: String, val outValue: Float)