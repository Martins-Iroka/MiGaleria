package com.martdev.android.data.pagingfactory

//import androidx.lifecycle.MutableLiveData
//import androidx.paging.DataSource
//import com.martdev.android.data.paging.PhotoDataPageSource
//import com.martdev.android.domain.photomodel.Photo
//import com.martdev.android.domain.photomodel.PhotoData
//import com.martdev.android.local.LocalDataSource
//import com.martdev.android.local.entity.PhotoDataEntity
//import com.martdev.android.local.entity.PhotoEntity
//import com.martdev.android.remote.RemoteDataSource
//import kotlinx.coroutines.CoroutineScope
//
//class PhotoPageDataSourceFactory(
//    private val query: String?,
//    private val localDataSource: LocalDataSource<PhotoEntity, PhotoDataEntity>,
//    private val remoteDataSource: RemoteDataSource<PhotoData>,
//    private val scope: CoroutineScope
//) : DataSource.Factory<Int, Photo>(){
//
//    val liveData = MutableLiveData<PhotoDataPageSource>()
//
//    override fun create(): DataSource<Int, Photo> {
//        val source = PhotoDataPageSource(query, localDataSource, remoteDataSource, scope)
//        liveData.postValue(source)
//        return source
//    }
//}