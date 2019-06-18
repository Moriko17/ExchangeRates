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

    private val bw = BookmarkWorker()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_third, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var marks = mutableListOf<Mark>()
        var text = ""

        bw.readFromFile(root.context)
        marks = bw.getMarks()

        marks.forEach{
            text = text + it.date +"  "+ it.inCur +"  "+ it.inValue.toString() +"  "+ it.outCur +"  "+ it.outValue.toString() + "\n"
        }

        mark.text = text
    }
}// Required empty public constructor