package com.bober.recipesapp.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: MyRecipesViewModel, recipeId: Int?, onBack: () -> Unit
) {
    val state by viewModel.state

    var editMode by remember { mutableStateOf(false) }

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
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            (state.selectedRecipe?.name ?: "Unknown") + ":",
                            style = MaterialTheme.typography.headlineLarge
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(
                            onClick = {
                                if (state.selectedRecipe != null) {
                                    editMode = !editMode
                                    if (editMode) {
                                    } else {
                                    }
                                }
                            }, enabled = state.selectedRecipe != null
                        ) {
                            Icon(
                                imageVector = if (editMode) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = if (editMode) "Save Changes" else "Edit Recipe"
                            )
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {viewModel.toggleIngredientChecked(ingredient.id)})
                            ) {
                                Checkbox(
                                    checked = ingredient.checked,
                                    onCheckedChange = {
                                        viewModel.toggleIngredientChecked(ingredient.id)
                                    },
                                )
                                Text(
                                    text = "${ingredient.name}: ${ingredient.quantity} ${ingredient.unit}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(16.dp))
                            Text(
                                text = state.selectedRecipe?.description ?: "",
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
