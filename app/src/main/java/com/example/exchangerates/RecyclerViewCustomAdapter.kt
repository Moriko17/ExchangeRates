package com.example.exchangerates

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.exchangerates.data.Mark

class RecyclerViewCustomAdapter(private val marksList: MutableList<Mark>):
    RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, position: Int) {
        p0.date.text = marksList[position].date
        p0.exchange.text = marksList[position].inValue.toString() + " " +
                marksList[position].inCur + " <--> " +
                marksList[position].outValue.toString() + " " +
                marksList[position].outCur
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.list_item, p0, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return marksList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val date: TextView = itemView.findViewById(R.id.date)
        val exchange: TextView = itemView.findViewById(R.id.exchange)
    }

}