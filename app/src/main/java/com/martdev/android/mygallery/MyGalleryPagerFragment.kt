package com.martdev.android.mygallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.martdev.android.mygallery.adapter.MyGalleryPagerAdapter
import com.martdev.android.mygallery.adapter.PHOTO_PAGE_INDEX
import com.martdev.android.mygallery.adapter.VIDEO_PAGE_INDEX
import com.martdev.android.mygallery.databinding.FragmentViewPagerBinding
import java.lang.IndexOutOfBoundsException

class MyGalleryPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager


        viewPager.adapter = MyGalleryPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    private fun getTabIcon(position: Int) = when(position) {
        PHOTO_PAGE_INDEX -> R.drawable.photo_tab_selector
        VIDEO_PAGE_INDEX -> R.drawable.video_tab_selector
        else -> throw IndexOutOfBoundsException()
    }

    private fun getTabTitle(position: Int): String? = when(position) {
        PHOTO_PAGE_INDEX -> "Photo"
        VIDEO_PAGE_INDEX -> "Video"
        else -> null
    }
}