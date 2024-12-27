package com.smedina.dive_log_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smedina.dive_log_app.data.FirebaseInstance
import com.smedina.dive_log_app.ui.theme.DivelogappTheme
import com.smedina.dive_log_app.ui.user.UserImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            firebaseInstance = FirebaseInstance(context)
            DivelogappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserImage(modifier = Modifier.padding(innerPadding))
                    Greeting("chaaacho!!") {firebaseInstance.writeOnFireBase() }
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, onClick:()->Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(top = 24.dp)
        )
        Button(onClick = { onClick.invoke() }) {
            Text(text = "Subir nuevo valor \t ;-) ")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DivelogappTheme {
        Greeting("Android"){}
    }
}