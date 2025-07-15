package com.bober.recipesapp.ui.alertdialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.TextField

@Composable
fun AddRecipeDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    recipeName: String,
) {
    var recipeName = recipeName
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add new recipe") },
        text = {
            TextField(value = recipeName,
                onValueChange = { recipeName = it },
                label = { Text("Section Name") })
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}
