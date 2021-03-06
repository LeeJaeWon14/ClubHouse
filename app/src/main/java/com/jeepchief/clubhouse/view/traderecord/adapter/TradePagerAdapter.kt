package com.jeepchief.clubhouse.view.traderecord.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TradePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = mutableListOf<Fragment>()
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        fragmentList[position].arguments = Bundle().apply {
            putInt("page", position)
        }
        return fragmentList[position]
    }

    fun addFragment(vararg fragment: Fragment) {
        fragment.forEach { frag ->
            fragmentList.add(frag)
        }
    }
}