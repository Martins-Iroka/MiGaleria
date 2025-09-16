package com.martdev.android.mygallery.utils


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.dev.adnetworkm.CheckNetworkStatus
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.mygallery.MyGalleryApp
import java.io.File

/*val PERMISSIONS = listOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)
const val PERMISSION_REQUEST_CODE = 1
const val DOWNLOAD_FILE_CODE = 2
fun Fragment.getViewModelFactory(): ViewModelFactory {
    val application = (requireContext().applicationContext as MyGalleryApp)
    val photoDataUseCase = application.photoUseCase
    val videoDataUseCase = application.videoUseCase

    return ViewModelFactory(photoDataUseCase, videoDataUseCase)
}

fun Fragment.setDownloadedFileName(fileName: String) {
    val folder = context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val file = File(folder, fileName)
    val uri = context?.let {
        FileProvider.getUriForFile(it, "${BuildConfig.APPLICATION_ID}.provider", file)
    }
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri?.path)
    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        setDataAndType(uri, mimeType)
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra(Intent.EXTRA_TITLE, fileName)
        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }
    startActivityForResult(intent, DOWNLOAD_FILE_CODE)
}

fun Fragment.viewFileExt(uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val chooser = Intent.createChooser(intent, "Open with")

    if (context?.packageManager?.let { intent.resolveActivity(it) } != null) {
        startActivity(chooser)
    } else Toast.makeText(context, "NO suitable application to open file", Toast.LENGTH_LONG).show()
}

fun FragmentActivity.checkNetworkState(): Boolean {
    var isNetworkConnected = false
    val toast = Toast.makeText(this, "No network connection", Toast.LENGTH_LONG)
    CheckNetworkStatus.getNetworkLiveData(applicationContext).observe(this, Observer {
        isNetworkConnected = when (it) {
            true -> {
                toast.cancel()
                it
            }
            false -> {
                toast.show()
                it
            }
        }
    })
    return isNetworkConnected
}

*//**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 *//*
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Any>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            when(it) {
                is Int -> showSnackbar(context.getString(it), timeLength)
                is String -> showSnackbar(it, timeLength)
            }
        }
    })
}

*//**
 * Transforms static java function Snackbar.make() to an extension function on View.
 *//*
private fun View.showSnackbar(
    snackbarText: String,
    timeLength: Int
) {
    Snackbar.make(this, snackbarText, timeLength).show()
}*/
