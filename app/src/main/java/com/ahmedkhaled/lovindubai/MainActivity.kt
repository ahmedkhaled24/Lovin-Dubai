package com.ahmedkhaled.lovindubai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ahmedkhaled.lovindubai.presentation.navigation.AppNavHost
import com.ahmedkhaled.lovindubai.ui.theme.LovinDubaiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LovinDubaiTheme {
                AppNavHost()
            }
        }
    }
}