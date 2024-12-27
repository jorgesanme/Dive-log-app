package com.smedina.dive_log_app.ui.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.smedina.dive_log_app.R
import com.smedina.dive_log_app.utils.GenerateFileUri


@Composable
fun UserImage(modifier: Modifier) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    var uri:Uri? by remember { mutableStateOf(null) }
    val intentCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()) {
        if(it && uri?.path?.isNotEmpty() == true){
            userViewModel.uploadBasicImage(uri!!)
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                uri = GenerateFileUri(context).generateUri()
                uri?.let {  intentCameraLauncher.launch(it) }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(painter = painterResource(R.drawable.add_a_photo_24), contentDescription = "", tint = Color.White)

        }



    }

}


