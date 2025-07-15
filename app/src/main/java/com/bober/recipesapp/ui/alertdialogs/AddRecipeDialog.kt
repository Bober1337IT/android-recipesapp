package com.bober.recipesapp.ui.alertdialogs

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddRecipeDialog(
    recipeName: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add new recipe") },
        text = {
            TextField(
                value = recipeName,
                onValueChange = onNameChange,
                label = { Text("Recipe name") },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (recipeName.isBlank()) {
                        Toast.makeText(
                            context,
                            "Recipe name cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        onConfirm()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

