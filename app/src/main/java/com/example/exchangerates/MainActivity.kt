package com.example.exchangerates

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageView
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val bw = BookmarkWorker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        val fragmentAdapter = PagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)

        tabs_main.getTabAt(0)!!.setIcon((R.drawable.ic_pulse))
        tabs_main.getTabAt(1)!!.setIcon(R.drawable.ic_exchange)
        tabs_main.getTabAt(2)!!.setIcon(R.drawable.ic_history)

    }

    override fun onStop() {
        bw.saveToJson(applicationContext)
        super.onStop()
    }

}