package com.bober.recipesapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bober.recipesapp.MyRecipesViewModel
import com.bober.recipesapp.ui.theme.RecipesAppTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: MyRecipesViewModel, recipeId: Int?, onBack: () -> Unit
) {
    val state by viewModel.state

    LaunchedEffect(recipeId) {
        recipeId?.let {
            viewModel.loadIngredients(it.toLong())
        }
    }

    RecipesAppTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Column {
                            Text(
                                state.recipeName + ":" , style = MaterialTheme.typography.headlineLarge
                            )
                        }
                        Column {
                            IconButton(
                                onClick = onBack
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        }
                    }
                })
        }, content = { paddingValues ->
            if (state.isLoading) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text("Loading...", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                if (state.ingredients.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No ingredients found.")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        items(state.ingredients, key = { it.id }) { ingredient ->
                            Text(
                                text = "${ingredient.name}: ${ingredient.quantity} ${ingredient.unit}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        })
    }
}
