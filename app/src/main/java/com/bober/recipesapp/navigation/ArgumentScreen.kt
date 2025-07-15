package com.bober.recipesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.bober.recipesapp.MyRecipesViewModel
import com.bober.recipesapp.navigation.arguments.addDetailsScreen
import com.bober.recipesapp.navigation.arguments.addMainScreen

sealed class Navigation(val route: String) {
    object MainScreen : Navigation("main_screen")
    object DetailScreen : Navigation("detail_screen/{recipeId}") {
        fun getRoute(recipeId: String) = "detail_screen/$recipeId"
    }
}

@Composable
fun ArgumentScreen(
    viewModel: MyRecipesViewModel,
    navHostController: NavHostController
){
    NavHost(
        navController = navHostController,
        startDestination = Navigation.MainScreen.route
    ) {
        addMainScreen(viewModel, navHostController)
        addDetailsScreen(viewModel, navHostController)
    }
}
