package com.bober.recipesapp.navigation.arguments

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bober.recipesapp.MyRecipesViewModel
import com.bober.recipesapp.navigation.Navigation
import com.bober.recipesapp.navigation.Navigation.MainScreen
import com.bober.recipesapp.ui.screens.MainScreen

internal fun NavGraphBuilder.addMainScreen(viewModel: MyRecipesViewModel, controller: NavController) {
    composable(MainScreen.route){
        MainScreen(viewModel) { recipeId ->
            controller.navigate(
                Navigation.DetailScreen.getRoute(recipeId.toString())
            )
        }
    }
}
