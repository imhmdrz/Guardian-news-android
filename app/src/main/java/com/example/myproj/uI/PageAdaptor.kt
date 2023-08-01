package com.example.myproj.uI

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PageAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 5;
    }
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> GuardianFragment("Home")
            1 -> GuardianFragment("World")
            2 -> GuardianFragment("Science")
            3 -> GuardianFragment("Sport")
            else -> GuardianFragment("Environment")
        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> return "Home"
            1 -> return "World"
            2 -> return "Science"
            3 -> return "Sport"
            4 -> return "Environment"
        }
        return super.getPageTitle(position)
    }
}
