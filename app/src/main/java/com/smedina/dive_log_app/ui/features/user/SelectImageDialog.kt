package com.smedina.dive_log_app.ui.features.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.smedina.dive_log_app.R
import com.smedina.dive_log_app.ui.theme.Purple40
import com.smedina.dive_log_app.ui.theme.Purple80

@Composable
fun SelectImageDialog(
    onDismissRequest: () -> Unit,
    onTakePhotoClicked: () -> Unit,
    onGetFromGalleryClicked: () -> Unit,
) {
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card(
            shape = RoundedCornerShape(12),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding( horizontal = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    stringResource(R.string.dialog_title),
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif
                )

                OutlinedButton (
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onTakePhotoClicked()
                        onDismissRequest()
                              },
                    border = BorderStroke(2.dp, Purple80)
                ) {
                    Text(
                        text = "Camera",
                        modifier = Modifier.padding(end = 16.dp),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif
                    )
                    Icon(
                        painter = painterResource(R.drawable.add_a_photo_24),
                        contentDescription = "",
                        tint = Purple40
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton (
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onGetFromGalleryClicked()
                        onDismissRequest()
                              },
                    border = BorderStroke(2.dp, Purple80)
                ) {
                    Text(
                        text = "Gallery",
                        modifier = Modifier.padding(end = 16.dp),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif
                    )
                    Icon(
                        painter = painterResource(R.drawable.gallery_add_photo_24),
                        contentDescription = "",
                        tint = Purple40
                    )
                }
            }
        }
    }
}