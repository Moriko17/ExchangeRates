package com.example.exchangerates

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = PagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)
    }
}