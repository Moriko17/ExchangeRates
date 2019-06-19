package com.example.exchangerates

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_third.*


/**
 * A simple [Fragment] subclass.
 */
class ThirdFragment : Fragment() {
    private lateinit var root: View
    private var loaded: Boolean = false

    private val bw = BookmarkWorker()
    private lateinit var marksToSave: MutableList<Mark>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_third, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loaded = true
        printMarks(true)
        userVisibleHint = true
    }

    override fun onPause() {
        super.onPause()
        bw.saveToJson(root.context)
        loaded = false
    }

    override fun onResume() {
        super.onResume()
        loaded = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && loaded) {
            printMarks()
        }
    }

    private fun printMarks() {
        val marks: MutableList<Mark> = bw.getMarks()
        var text = ""

//        bw.readFromFile(root.context)

        marks.forEach{
            text = text + it.date +"  "+ it.inCur +"  "+ it.inValue.toString() +"  "+ it.outCur +"  "+ it.outValue.toString() + "\n"
        }

        mark.text = text

        marksToSave = marks
    }

    private fun printMarks(readFromFile: Boolean) {

        if (readFromFile) {
            bw.readFromFile(root.context)
            printMarks()
        }
    }
}// Required empty public constructor