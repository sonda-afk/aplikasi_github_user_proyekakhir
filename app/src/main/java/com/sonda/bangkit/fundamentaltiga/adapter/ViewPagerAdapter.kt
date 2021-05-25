package com.sonda.bangkit.fundamentaltiga.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sonda.bangkit.fundamentaltiga.ui.FollowersFragment
import com.sonda.bangkit.fundamentaltiga.ui.FollowingFragment

class ViewPagerAdapter (activity: AppCompatActivity) :  FragmentStateAdapter(activity) {



    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

}