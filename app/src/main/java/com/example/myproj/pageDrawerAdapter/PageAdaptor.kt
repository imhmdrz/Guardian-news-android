package com.example.myproj.pageDrawerAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myproj.uiHolder.GuardianFragment
import com.example.myproj.uiHolder.setting.SettingFragment

class PageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> GuardianFragment.newInstance("Home")
            1 -> GuardianFragment.newInstance("World")
            2 -> GuardianFragment.newInstance("Science")
            3 -> GuardianFragment.newInstance("Sport")
            else -> GuardianFragment.newInstance("Environment")
        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Home"
            1 -> return "World"
            2 -> return "Science"
            3 -> return "Sport"
            4 -> return "Environment"
        }
        return super.getPageTitle(position)
    }
}
