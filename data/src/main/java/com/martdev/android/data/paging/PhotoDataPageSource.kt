package com.martdev.android.data.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.martdev.android.data.toVideoEntity
import com.martdev.android.data.toSrcEntity
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.PhotoDataSource
import com.martdev.android.local.entity.PhotoDataEntity
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoDataPageSource(
    private val query: String?,
    private val localDataSource: LocalDataSource<PhotoEntity, PhotoDataEntity>,
    private val remoteDataSource: RemoteDataSource<PhotoData>,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Photo>() {

    var networkState = MutableLiveData<Result<List<Photo>>>()

    var retry: RetryCallback? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        fetchData(per_page = params.requestedLoadSize, page = 1, callback = {
            callback.onResult(it, null, 2)
        }, retry = {
            retry = object : RetryCallback {
                override fun invoke() {
                    loadInitial(params, callback)
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        val page = params.key
        fetchData(params.requestedLoadSize, page = page, callback =  {photoData ->
            callback.onResult(photoData, page + 1)
        }, retry = {
            retry = object : RetryCallback {
                override fun invoke() {
                    loadAfter(params, callback)
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        TODO("Not yet implemented")
    }

    private fun fetchData(per_page: Int, page: Int, callback: (List<Photo>) -> Unit, retry: () -> Unit) {
        scope.launch {
            Log.d(PhotoDataPageSource::class.java.simpleName, "Entering withContext")
            withContext(Dispatchers.IO) {
                val result = if (query.isNullOrEmpty()) {
                    remoteDataSource.load(per_page, page)
                } else remoteDataSource.search(query, per_page, page)

                when (result.status) {
                    Result.Status.LOADING -> networkState.value = Result.loading()
                    Result.Status.SUCCESS -> {
                        val data = result.data?.photos!!
                        val source = localDataSource as PhotoDataSource
                        source.deleteData()
                        Log.d(PhotoDataPageSource::class.java.simpleName, data.toString())
                        networkState.postValue(Result.success(data))
                            data.forEach {photo ->
                                source.saveData(photo.toVideoEntity())
                                source.savePhotoSrc(photo.src.toSrcEntity(photo.id))
                            }
                        callback(data)
                        }
                    Result.Status.ERROR -> {
                        networkState.postValue(Result.error(result.message))
                        Log.e(PhotoDataPageSource::class.java.simpleName, result.message)
                        retry()
                    }
                }
            }
        }
    }
}