package com.bober.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.bober.recipesapp.navigation.ArgumentScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: MyRecipesViewModel by viewModels()
        setContent {
            val navController = rememberNavController()
            ArgumentScreen(
                viewModel = viewModel,
                navHostController = navController
            )
        }
    }
}