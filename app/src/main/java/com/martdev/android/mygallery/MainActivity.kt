package com.martdev.android.mygallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Validators.and
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dev.adnetworkm.CheckNetworkStatus
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.mygallery.databinding.ActivityMainBinding
import com.martdev.android.mygallery.fragment.PhotoListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}