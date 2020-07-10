package com.martdev.android.mygallery

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.martdev.android.mygallery.utils.PERMISSIONS
import com.martdev.android.mygallery.utils.PERMISSION_REQUEST_CODE

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasPermissions(this, PERMISSIONS))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PERMISSIONS.toTypedArray(), PERMISSION_REQUEST_CODE)
            }
    }

    private fun hasPermissions(context: Context?, permissions: List<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            return permissions.all { permission ->
                ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}