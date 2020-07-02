package com.martdev.android.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.martdev.android.data.toEntity
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoDataPageSource(
    private val query: String?,
    private val localDataSource: LocalDataSource<PhotoEntity>,
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
            withContext(Dispatchers.IO) {
                val result = if (query.isNullOrEmpty()) {
                    remoteDataSource.load(per_page, page)
                } else remoteDataSource.search(query, 15, page)

                when (result.status) {
                    Result.Status.LOADING -> networkState.value = Result.loading()
                    Result.Status.SUCCESS -> {
                        localDataSource.deleteData()
                        val data = result.data?.photos!!
                        networkState.postValue(Result.success(data))
                            data.forEach {photo ->
                                localDataSource.saveData(photo.toEntity())
                            }
                        callback(data)
                        }
                    Result.Status.ERROR -> {
                        networkState.postValue(Result.error(result.message))
                        retry()
                    }
                }
            }
        }
    }
}