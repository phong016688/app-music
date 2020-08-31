package com.example.sunmusic.screen.main

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    private val items: List<MainTabItem>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = items[position].fragment

    override fun getCount() = items.size

    override fun getPageTitle(position: Int): CharSequence? = items[position].title
}
