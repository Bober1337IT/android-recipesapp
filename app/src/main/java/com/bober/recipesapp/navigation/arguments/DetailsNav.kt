package com.bober.recipesapp.navigation.arguments

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bober.recipesapp.MyRecipesViewModel
import com.bober.recipesapp.navigation.Navigation
import com.bober.recipesapp.ui.screens.DetailScreen

internal fun NavGraphBuilder.addDetailsScreen(viewModel: MyRecipesViewModel,controller: NavController) {
    composable(
        Navigation.DetailScreen.route,
        arguments = listOf(
            navArgument("recipeId") {
                type = NavType.IntType
            }
        )
    ) { navBackStackEntry ->
        val recipeId = navBackStackEntry.arguments?.getInt("recipeId")

        DetailScreen(viewModel, recipeId) {
            controller.popBackStack()
        }
    }
}