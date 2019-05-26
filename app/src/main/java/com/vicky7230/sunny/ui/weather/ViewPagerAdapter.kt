package com.vicky7230.sunny.ui.weather

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.vicky7230.sunny.ui.base.BaseFragment

class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    private val fragmentList = mutableListOf<BaseFragment>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItemPosition(any: Any): Int {
        return PagerAdapter.POSITION_NONE//highly inefficient code.......
    }

    fun addFragment(fragment: BaseFragment) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }

    fun removeFragments() {
        fragmentList.clear()
        notifyDataSetChanged()
    }
}
