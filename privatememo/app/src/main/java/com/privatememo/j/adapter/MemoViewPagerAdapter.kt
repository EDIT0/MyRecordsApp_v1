package com.privatememo.j.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.privatememo.j.ui.bottombar.memo.CategoryFragment
import com.privatememo.j.ui.bottombar.memo.OnlyPicFragment

class MemoViewPagerAdapter : FragmentStateAdapter {

    constructor(activity: FragmentActivity):super(activity)

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        when(position){
            0 -> {
                return CategoryFragment()
            }
            1 -> {
                return OnlyPicFragment()
            }
        }

        return CategoryFragment()
    }


}