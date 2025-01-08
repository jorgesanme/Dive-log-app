package com.smedina.dive_log_app.ui.features.user

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.smedina.dive_log_app.R
import com.smedina.dive_log_app.ui.features.main.ScreenTitle
import com.smedina.dive_log_app.ui.theme.Purple40
import com.smedina.dive_log_app.ui.theme.Purple80
import com.smedina.dive_log_app.utils.GenerateFileUri


@Composable
fun UserImageScreen() {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    var uri: Uri? by remember { mutableStateOf(null) }
    val userImage by userViewModel.userImage.collectAsStateWithLifecycle()
    val isLoading by userViewModel.isLoading.collectAsStateWithLifecycle()
    var showImageDialog: Boolean by remember { mutableStateOf(false) }

    var userTitle by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val intentCameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it && uri?.path?.isNotEmpty() == true) {
                userTitle = ""
                focusManager.clearFocus()
                userViewModel.uploadAndGetImage(uri!!)
            }
        }
    val intentGalleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it?.path?.isNotEmpty() == true) {
            userTitle = ""
            focusManager.clearFocus()
            userViewModel.uploadAndGetImage(it)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle("USER")

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
        Card(
            elevation = CardDefaults.cardElevation(12.dp),
            shape = RoundedCornerShape(12),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 24.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = colorResource(R.color.purple_200)
                    )
                }
            }

            if (userImage != null) {
                AsyncImage(
                    model = userImage,
                    contentDescription = "UserImage",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            if (!isLoading && userImage == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painterResource(R.drawable.add_a_photo_24),
                        contentDescription = "upload user image",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            value = userTitle,
            onValueChange = { userTitle = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .focusRequester(focusRequester),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Purple40,
                unfocusedBorderColor = Purple80,
                cursorColor = Purple40

            ),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            label = { Text("Image title") },
            trailingIcon = {
                if (userTitle.isNotEmpty()) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = "clear title",
                        tint = Purple40,
                        modifier = Modifier.clickable { userTitle = "" }
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            OutlinedButton(
                onClick = { showImageDialog = true },
                border = BorderStroke(2.dp, Purple80)
            ) {
                Icon(
                    painter = painterResource(R.drawable.account_circle_24),
                    contentDescription = "",
                    tint = Purple40
                )
                Text(text = "Image", modifier = Modifier.padding(start = 16.dp))
            }

            OutlinedButton(
                onClick = {
                    if (userTitle.isNotEmpty()) {
                        userViewModel.deletePhoto(userTitle)
                        Toast.makeText(context, "Img: $userTitle, DELETED", Toast.LENGTH_SHORT)
                            .show()
                        userTitle = ""
                        focusManager.clearFocus()

                    } else {
                        Toast.makeText(context, "No image name", Toast.LENGTH_SHORT).show()
                    }
                },
                border = BorderStroke(2.dp, Purple80)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "",
                    tint = Purple40
                )
                Text(text = "remove", modifier = Modifier.padding(start = 16.dp))
            }
        }
    }
}




