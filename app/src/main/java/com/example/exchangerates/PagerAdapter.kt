package com.example.exchangerates

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.exchangerates.fragments.FirstFragment
import com.example.exchangerates.fragments.SecondFragment
import com.example.exchangerates.fragments.ThirdFragment

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            2 -> {
                ThirdFragment()
            }
            1 -> {
                SecondFragment()
            }
            else -> {
                return FirstFragment()
            }
        }
    }

    override fun getCount(): Int {

        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> ""
            1 -> ""
            else -> {
                return ""
            }
        }
    }
}
