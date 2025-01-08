package com.smedina.dive_log_app.ui.features.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.smedina.dive_log_app.R
import com.smedina.dive_log_app.ui.features.user.SelectImageDialog
import com.smedina.dive_log_app.ui.theme.DivelogappTheme
import com.smedina.dive_log_app.ui.theme.Purple40
import com.smedina.dive_log_app.utils.GenerateFileUri

@Composable
fun MainScreen() {
    val mainViewModel: MainViewModel = hiltViewModel()
    var uri: Uri? by remember { mutableStateOf(null) }
    val uriList by mainViewModel.uriList.collectAsStateWithLifecycle()
    val isLoading by mainViewModel.isLoading.collectAsStateWithLifecycle()
    var showImageDialog: Boolean by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val intentCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { it ->

            if (it && uri?.path?.isNotEmpty() == true) {
                mainViewModel.uploadAndGetImage(uri!!)
            }
        }
    val intentGalleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it?.path?.isNotEmpty() == true) {
            mainViewModel.uploadAndGetImage(it)
        }
    }

    LaunchedEffect(key1 = Unit) { mainViewModel.getAllImage() }

    DivelogappTheme {
        Scaffold(
            floatingActionButton = { MainFloatingActionButton { showImageDialog = true } },
            floatingActionButtonPosition = FabPosition.Center
        ) { innerPadding ->
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item { ScreenTitle("Main") }
                item {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(60.dp)
                                .padding(vertical = 48.dp),
                            color = colorResource(R.color.purple_200)
                        )
                    }
                }
                item {
                    if (showImageDialog) {
                        SelectImageDialog(
                            onDismissRequest = { showImageDialog = false },
                            onTakePhotoClicked = {
                                uri = GenerateFileUri(context).generateUri()
                                uri?.let { intentCameraLauncher.launch(it) }
                            },
                            onGetFromGalleryClicked = {
                                intentGalleryLauncher.launch("image/*")
                            }
                        )
                    }
                }
                items(uriList, key = { it }) {
                    if (isLoading) {
                        Spacer(modifier = Modifier.height(24.dp))
                    } else {
                        ItemImage(it) {
                            mainViewModel.deletePhoto(it)
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ItemImage(image: String, onRemoved: () -> Unit) {
    ListItem(
        headlineContent = { Text("HeadLine Content") },
        overlineContent = { Text("Texto de arriba overline") },
        supportingContent = { Text("texto de abajo") },
        trailingContent = {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "",
                modifier = Modifier.clickable(
                    enabled = true,
                    role = Role.Button,
                    onClick = { onRemoved() }
                )
            )
        },
        leadingContent = {
            Card(
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12),
                modifier = Modifier
                    .size(150.dp)
                    .padding(horizontal = 8.dp)
            ) {
                AsyncImage(
                    model = image,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "image"
                )
            }
        }
    )
}


@Composable
fun MainFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        { onClick() }
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = "",
            tint = Purple40
        )
    }

}

@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(top = 45.dp)
    )

}


