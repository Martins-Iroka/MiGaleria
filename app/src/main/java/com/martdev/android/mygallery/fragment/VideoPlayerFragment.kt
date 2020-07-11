package com.martdev.android.mygallery.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.martdev.android.mygallery.videoplayerutils.PlayerHolder
import com.martdev.android.mygallery.videoplayerutils.PlayerState
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.databinding.FragmentPlayerViewBinding

class VideoPlayerFragment : Fragment() {

    private val args: VideoPlayerFragmentArgs by navArgs()
    private lateinit var playerHolder: PlayerHolder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPlayerViewBinding>(inflater,
        R.layout.fragment_player_view, container, false)

        val state =
            PlayerState(source = args.stringUri)
        playerHolder =
            PlayerHolder(
                requireActivity(),
                binding.exoplayerview,
                state
            )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        playerHolder.start()
    }

    override fun onStop() {
        super.onStop()
        playerHolder.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerHolder.release()
    }
}