package com.bober.recipesapp.entity

data class Ingredient(
    val id: Long,
    val recipeId: Long,
    val name: String,
    val quantity: Double,
    val unit: String
)