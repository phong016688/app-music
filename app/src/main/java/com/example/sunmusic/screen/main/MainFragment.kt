package com.example.sunmusic.screen.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.sunmusic.R
import com.example.sunmusic.screen.album.AlbumFragment
import com.example.sunmusic.screen.genres.GenresFragment
import com.example.sunmusic.screen.home.HomeFragment
import com.example.sunmusic.screen.trending.TrendingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.layout_search.view.*

class MainFragment : Fragment(R.layout.fragment_main), ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val pagerAdapter by lazy { MainPagerAdapter(childFragmentManager, getListTabItem()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        view?.mainViewPager?.apply {
            adapter = pagerAdapter
            offscreenPageLimit = pagerAdapter.count
            addOnPageChangeListener(this@MainFragment)
        }
        view?.searchLayout?.searchView?.apply {
            onActionViewExpanded()
            clearFocus()
        }
        view?.mainBottomNavigation?.setOnNavigationItemSelectedListener(this@MainFragment)
    }

    private fun getListTabItem() = listOf(
        MainTabItem(requireContext().getString(R.string.home), HomeFragment.newInstance()),
        MainTabItem(requireContext().getString(R.string.trending), TrendingFragment.newInstance()),
        MainTabItem(requireContext().getString(R.string.album), AlbumFragment.newInstance()),
        MainTabItem(requireContext().getString(R.string.genres), GenresFragment.newInstance())
    )

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        view?.apply {
            val positionTabItemSelected = getPositionTabItem(item.itemId)
            if (mainViewPager.currentItem != positionTabItemSelected) {
                mainViewPager.currentItem = positionTabItemSelected
            }
        }
        return true
    }

    private fun getPositionTabItem(@IdRes itemId: Int) = when (itemId) {
        R.id.home -> MainTabItem.POSITION_HOME
        R.id.trending -> MainTabItem.POSITION_TRENDING
        R.id.album -> MainTabItem.POSITION_ALBUM
        R.id.genres -> MainTabItem.POSITION_GENRES
        else -> MainTabItem.POSITION_HOME
    }

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(p: Int, pOffset: Float, pOffsetPixels: Int) = Unit

    override fun onPageSelected(position: Int) {
        view?.mainBottomNavigation?.menu?.getItem(position)?.isChecked = true
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
