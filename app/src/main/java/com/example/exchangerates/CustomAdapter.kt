package com.example.exchangerates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CustomAdapter(val marksList: MutableList<Mark>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onBindViewHolder(p0: ViewHolder, position: Int) {
        p0.date?.text = marksList[position].date
        p0.exchange?.text = marksList[position].inValue.toString() + marksList[position].inCur + " <-->" + marksList[position].outValue.toString() + marksList[position].outCur

    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.list_item, p0, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return marksList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.date)
        val exchange = itemView.findViewById<TextView>(R.id.exchange)

    }

}