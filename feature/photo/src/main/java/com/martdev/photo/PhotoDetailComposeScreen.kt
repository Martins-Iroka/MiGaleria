@file:OptIn(ExperimentalMaterial3Api::class)

package com.martdev.photo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martdev.common.convertUTCToLocalDateTime
import com.martdev.domain.ResponseData
import com.martdev.domain.photodata.PhotoPostComments
import com.martdev.ui.reusable.CustomLayout
import com.martdev.ui.reusable.TextCompose
import org.koin.androidx.compose.koinViewModel

//write unit and ui test for the new implementations
//refactor the userId storing logic
@Composable
fun PhotoDetailCompose(
    postID: Long,
    imageUrl: String,
    goBack: () -> Unit = {}
) {

    val viewModel: PhotoViewModel = koinViewModel()

    val photoPostComments by viewModel.photoComments.collectAsStateWithLifecycle(
        ResponseData.NoResponse
    )
    val sendCommentResponse by viewModel.createCommentsResponse.collectAsStateWithLifecycle()

    BackHandler {
        goBack()
    }

    LaunchedEffect(Unit) {
        viewModel.getCommentsByPostId(postID.toString())
    }
    PhotoDetail(imageUrl, photoPostComments, sendCommentResponse) {
        viewModel.postComment(postID.toString(), it)
    }
}

@Composable
internal fun PhotoDetail(
    imageUrl: String,
    comments: ResponseData<List<PhotoPostComments>> = ResponseData.NoResponse,
    sendCommentResponse: ResponseData<Nothing> = ResponseData.NoResponse,
    sendComment: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(true)

    var comment by remember {
        mutableStateOf("")
    }

    CustomLayout(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(it).fillMaxSize()
            .background(Color.Black).padding(16.dp)) {

            AsyncImage(model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
                contentDescription = imageUrl,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            IconButton(onClick = {
                showBottomSheet = true
            },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Comment,
                    "",
                    tint = Color.White
                )
            }

            if (showBottomSheet) {
                ModalBottomSheet(onDismissRequest = {
                    showBottomSheet = false
                }, sheetState = sheetState, contentWindowInsets = {
                    WindowInsets.ime
                }) {
                    Scaffold(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).imePadding(),
                        bottomBar = {
                            OutlinedTextField(value = comment, onValueChange = {
                                comment = it
                            }, modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding().imePadding(),
                                trailingIcon = {
                                    if (sendCommentResponse is ResponseData.Loading) {
                                        CircularProgressIndicator()
                                    } else {
                                        IconButton(onClick = {
                                            sendComment(comment)
                                            comment = ""
                                        }) {
                                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                                        }
                                    }
                                })
                        }) { innerPadding ->
                        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            when (comments) {
                                is ResponseData.Loading -> {
                                    item {
                                        CircularProgressIndicator()
                                    }
                                }

                                is ResponseData.Success -> {
                                    if (comments.data.isNullOrEmpty()) {
                                        item {
                                            Column(
                                                Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally) {

                                                TextCompose("Nothing to see")

                                            }
                                        }
                                    } else {
                                        items(items = comments.data.orEmpty(), key = {
                                            it.id
                                        }) {
                                            CommentCompose(it.content, it.username, it.createdAt)
                                        }
                                    }
                                }

                                is ResponseData.Error -> {
                                    item {
                                        Column(
                                            Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally) {

                                            TextCompose(comments.message)

                                        }
                                    }
                                }
                                ResponseData.NoResponse -> {
                                    item {
                                        Column(
                                            Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally) {

                                            TextCompose("Nothing to see")

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentCompose(content: String, username: String, createdAt: String) {
    val localTime = remember(createdAt) {
        try {
            convertUTCToLocalDateTime(createdAt)
        } catch (e: Exception) {
            e.printStackTrace()
            createdAt
        }
    }
    Column(Modifier.fillMaxWidth()) {
        TextCompose(
            text = content,
            textColor = Color.Black
        )
        TextCompose(
            text = "By $username at $localTime",
            textColor = Color.DarkGray
        )
    }
}

@Preview
@Composable
private fun PhotoDetailPreview() {
    PhotoDetail("https://images.pexels.com/photos/34611213/pexels-photo-34611213.jpeg")
}