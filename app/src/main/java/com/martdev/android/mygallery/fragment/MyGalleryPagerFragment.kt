package com.martdev.android.mygallery.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.adapter.MyGalleryPagerAdapter
import com.martdev.android.mygallery.adapter.PHOTO_PAGE_INDEX
import com.martdev.android.mygallery.adapter.VIDEO_PAGE_INDEX
import com.martdev.android.mygallery.databinding.FragmentViewPagerBinding
import com.martdev.android.mygallery.utils.*
import com.martdev.android.mygallery.viewmodel.PhotoViewModel
import com.martdev.android.mygallery.viewmodel.VideoViewModel
import java.lang.IndexOutOfBoundsException

class MyGalleryPagerFragment : Fragment(){

   /* private lateinit var binding: FragmentViewPagerBinding
    private val photoViewModel: PhotoViewModel by activityViewModels { getViewModelFactory() }
    private val videoViewModel: VideoViewModel by activityViewModels { getViewModelFactory() }

    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_view_pager, container, false)
        tabLayout = binding.tabs
        val viewPager = binding.viewPager


        viewPager.adapter = MyGalleryPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        val toolbar = binding.toolbar
        (activity as AppCompatActivity).run {
            setSupportActionBar(toolbar)
        }

        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.keyword_search, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    when(tabLayout.selectedTabPosition) {
                        PHOTO_PAGE_INDEX -> photoViewModel.getData(query)
                        VIDEO_PAGE_INDEX -> videoViewModel.getData(query)
                    }
                    searchView.onActionViewCollapsed()
                    true
                } else false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)
    }*/
}