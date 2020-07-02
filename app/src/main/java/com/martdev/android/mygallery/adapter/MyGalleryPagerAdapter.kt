package com.martdev.android.mygallery.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.martdev.android.mygallery.fragment.PhotoListFragment
import com.martdev.android.mygallery.fragment.VideoListFragment
import java.lang.IndexOutOfBoundsException

const val PHOTO_PAGE_INDEX = 0
const val VIDEO_PAGE_INDEX = 1

class MyGalleryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        PHOTO_PAGE_INDEX to { PhotoListFragment() },
        VIDEO_PAGE_INDEX to { VideoListFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}