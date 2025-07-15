package com.bober.recipesapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bober.recipesapp.MyRecipesViewModel
import com.bober.recipesapp.ui.alertdialogs.DeleteRecipeDialog
import com.bober.recipesapp.ui.theme.RecipesAppTheme
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.*
import com.bober.recipesapp.ui.alertdialogs.AddRecipeDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MyRecipesViewModel, onClick: (Int) -> Unit
) {
    val state by viewModel.state

    var dropDownMenuExpanded by remember { mutableStateOf(false) }

    var deleteRecipeToggle by remember { mutableStateOf(false) }
    var deleteRecipeDialogVisible by remember { mutableStateOf(false) }
    var recipeIdToDelete by remember { mutableStateOf<Long?>(null) }
    var disappearingId by remember { mutableStateOf<Long?>(null) }

    var addRecipeDialogVisible by remember { mutableStateOf(false) }
    var recipeName by remember { mutableStateOf("") }

    RecipesAppTheme {
        Scaffold(
            topBar = {
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
                                    "My Recipes",
                                    style = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Column {
                                IconButton(
                                    onClick = {
                                        if (deleteRecipeToggle) {
                                            deleteRecipeToggle = false
                                        } else {
                                            dropDownMenuExpanded = !dropDownMenuExpanded
                                        }
                                    }, modifier = Modifier.padding(8.dp)
                                ) {
                                    if (deleteRecipeToggle) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Cancel"
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "More Options Icon"
                                        )
                                    }
                                }
                                DropdownMenu(
                                    expanded = dropDownMenuExpanded,
                                    onDismissRequest = { dropDownMenuExpanded = false },
                                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
                                ) {
                                    DropdownMenuItem(onClick = {
                                        dropDownMenuExpanded = false
                                        addRecipeDialogVisible = true
                                    }, text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Add Icon"
                                            )
                                            Text("Add")
                                        }
                                    })
                                    DropdownMenuItem(onClick = {
                                        deleteRecipeToggle = !deleteRecipeToggle
                                        dropDownMenuExpanded = false
                                    }, text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Icon"
                                            )
                                            Text("Delete")
                                        }
                                    })
                                }
                            }
                        }
//                        Button(onClick = { viewModel.deleteAllRecipes() }) { //do testowania
//                            Text("Delete All Recipes")
//                        }
                    })
            }) { paddingValues ->

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
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(state.recipes.sortedBy { it.name }, key = { it.id }) { recipe ->

                        val isVisible = recipe.id != disappearingId

                        AnimatedVisibility(visible = isVisible) {
                            val backgroundColor =
                                if (deleteRecipeToggle) MaterialTheme.colorScheme.errorContainer
                                else MaterialTheme.colorScheme.primaryContainer

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .background(
                                        color = backgroundColor, shape = RoundedCornerShape(24.dp)
                                    )
                                    .clickable {
                                        if (deleteRecipeToggle) {
                                            recipeIdToDelete = recipe.id
                                            deleteRecipeDialogVisible = true
                                        } else {
                                            viewModel.selectRecipe(recipe)
                                            onClick(recipe.id.toInt())
                                        }
                                    }
                                    .padding(16.dp)) {
                                Text(
                                    text = recipe.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        if (disappearingId == recipe.id) {
                            LaunchedEffect(recipe.id) {
                                delay(300)
                                viewModel.deleteRecipe(recipe.id)
                                disappearingId = null
                                deleteRecipeToggle = false
                            }
                        }
                    }
                }
            }

            if (deleteRecipeDialogVisible) {
                DeleteRecipeDialog(onDismiss = {
                    deleteRecipeDialogVisible = false
                    recipeIdToDelete = null
                    deleteRecipeToggle = false
                }, onConfirm = {
                    recipeIdToDelete?.let { id ->
                        disappearingId = id
                    }
                    deleteRecipeDialogVisible = false
                    deleteRecipeToggle = false
                })
            }
            if (addRecipeDialogVisible) {
                AddRecipeDialog(
                    recipeName = recipeName,
                    onNameChange = { recipeName = it },
                    onDismiss = {
                        addRecipeDialogVisible = false
                        recipeName = ""
                    },
                    onConfirm = {
                        if (recipeName.isNotBlank()) {
                            viewModel.addRecipe(recipeName)
                        }
                        addRecipeDialogVisible = false
                        recipeName = ""
                    })
            }
        }
    }
}
