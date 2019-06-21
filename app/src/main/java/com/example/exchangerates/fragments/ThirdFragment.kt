package com.example.exchangerates.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.exchangerates.BookmarkWorker
import com.example.exchangerates.R
import com.example.exchangerates.RecyclerViewCustomAdapter
import com.example.exchangerates.data.Mark
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : Fragment() {
    private lateinit var root: View

    private val bw = BookmarkWorker()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_third, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView1.layoutManager = LinearLayoutManager(root.context, LinearLayout.VERTICAL, false)

        bw.readFromFile(root.context)
        val marks: MutableList<Mark> = bw.getMarks()

        val adapter = RecyclerViewCustomAdapter(marks.asReversed())
        recyclerView1.adapter = adapter
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser ) {

            recyclerView1.layoutManager = LinearLayoutManager(root.context, LinearLayout.VERTICAL, false)

            val marks: MutableList<Mark> = bw.getMarks()

            val adapter = RecyclerViewCustomAdapter(marks.asReversed())
            recyclerView1.adapter = adapter
        }
    }

}