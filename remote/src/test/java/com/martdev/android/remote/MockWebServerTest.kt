package com.martdev.android.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.remote.remotephoto.PhotoRemoteDataSource
import com.martdev.android.remote.remotevideo.VideoRemoteDataSource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.FileInputStream
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MockWebServerTest {

    private lateinit var server: MockWebServer
    private val MOCK_WEBSERVER_PORT = 8000

    private lateinit var apiService: ApiService
    private lateinit var photoDataSource: RemoteDataSource<PhotoData>
    private lateinit var videoDataSource: RemoteDataSource<VideoData>

    private val nature = "nature"
    private val per_page = 15
    private val page = 1

    @Before
    fun init() {
        server = MockWebServer()
        server.start(MOCK_WEBSERVER_PORT)

        val moshi =  Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(ApiService::class.java)

        photoDataSource = PhotoRemoteDataSource(apiService)
        videoDataSource = VideoRemoteDataSource(apiService)
    }

    @After
    fun shutdown() = server.shutdown()

    @Test
    fun searchPhoto_returnPhotoList() {
        runBlockingTest {

            enqueueResponse()
            val result = photoDataSource.search(nature, per_page, page)

            assertThat(result.status, `is`(Result.Status.SUCCESS))
//            assertNotNull(result.data)
        }
    }

    private val path = "C:/Users/Martins/AndroidStudioProjects/MartDev/MyGallery/remote/src/main/res/raw/search_photo.json"
    private fun enqueueResponse() {
        val inSt = FileInputStream(path) as InputStream
        val inputStream = InputStreamReader(inSt)
        val mockResponse = MockResponse()

        server.enqueue(mockResponse.setBody(
            inputStream.readText())
        )
        inputStream.close()
    }
}