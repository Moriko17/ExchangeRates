package com.example.exchangerates

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : Fragment() {
    private lateinit var root: View
    private var loaded: Boolean = false

    private val bw = BookmarkWorker()
    private lateinit var marksToSave: MutableList<Mark>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_third, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loaded = true
//        printMarks(true)
        userVisibleHint = true

        val rv = root.findViewById<RecyclerView>(R.id.recyclerView1)
        rv.layoutManager = LinearLayoutManager(root.context, LinearLayout.VERTICAL, false)

        bw.readFromFile(root.context)
        val marks: MutableList<Mark> = bw.getMarks()

        val adapter = CustomAdapter(marks.asReversed())
        rv.adapter = adapter
    }

    override fun onPause() {
        super.onPause()
        bw.saveToJson(root.context)
        loaded = false
    }

    override fun onResume() {
        super.onResume()
        loaded = true

//        recyclerView1.layoutManager = LinearLayoutManager(root.context, LinearLayout.VERTICAL, false)
//
//        val marks: MutableList<Mark> = bw.getMarks()
//        marks.reverse()
//
//        val adapter = CustomAdapter(marks)
//        recyclerView1.adapter = adapter
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && loaded) {
//            printMarks()

            recyclerView1.layoutManager = LinearLayoutManager(root.context, LinearLayout.VERTICAL, false)

            val marks: MutableList<Mark> = bw.getMarks()

            val adapter = CustomAdapter(marks.asReversed())
            recyclerView1.adapter = adapter
        }
    }
//
//    private fun printMarks() {
//        val marks: MutableList<Mark> = bw.getMarks()
//        var text = ""
//
////        bw.readFromFile(root.context)
//
//        marks.forEach{
//            text = text + it.date +"  "+ it.inCur +"  "+ it.inValue.toString() +"  "+ it.outCur +"  "+ it.outValue.toString() + "\n"
//        }
//
//        mark.text = text
//
//        marksToSave = marks
//    }
//
//    private fun printMarks(readFromFile: Boolean) {
//
//        if (readFromFile) {
//            bw.readFromFile(root.context)
//            printMarks()
//        }
//    }
}