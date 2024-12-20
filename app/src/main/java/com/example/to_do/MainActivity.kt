package com.example.to_do

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.to_do.ui.theme.TODOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
   Scaffold(
       topBar = {
           TopAppBar(title = { Text(text = "ToDo")}

           )
       }, content = { innerPadding ->
           Column(modifier = Modifier.padding(innerPadding)) {

           }
       }
   )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TODOTheme {
        MainPage()
    }
}