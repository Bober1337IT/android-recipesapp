package com.bober.recipesapp.database

import com.bober.recipesapp.entity.Ingredient
import com.bober.recipesapp.entity.Recipe

internal class Database(private val databaseDriverFactory: AndroidDatabaseDriverFactory) {
    private val database = RecipesDatabase(databaseDriverFactory.createDriver())

    private val dbQuery = database.recipesDatabaseQueries

    internal fun getAllRecipes(): List<Recipe> {
        println("Loaded recipes from DB: ${dbQuery.selectAllRecipes(::mapRecipeSelecting).executeAsList()}")
        return dbQuery.selectAllRecipes(::mapRecipeSelecting).executeAsList()
    }

    internal fun selectIngredientsByRecipeId(recipeId: Long): List<Ingredient> {
        return dbQuery.selectIngredientsForRecipe(recipeId, ::mapIngredientsSelecting).executeAsList()
    }

    internal fun insertRecipe(name: String, description: String?) {
        dbQuery.insertRecipe(name, description)
    }

    internal fun insertIngredient(
        recipeId: Long,
        name: String,
        quantity: Double,
        unit: String
    ) {
        dbQuery.insertIngredient(recipeId, name, quantity, unit)
    }

    internal fun deleteRecipe(id: Long) {
        dbQuery.transaction {
            dbQuery.deleteRecipe(id)
        }
    }

    internal fun deleteAllRecipes() {
        dbQuery.transaction {
            dbQuery.deleteAllRecipes()
        }
    }

    private fun mapRecipeSelecting(
        id: Long,
        name: String,
        description: String?
    ): Recipe {
        return Recipe(id, name, description)
    }

    private fun mapIngredientsSelecting(
        id: Long,
        recipeId: Long,
        name: String,
        quantity: Double,
        unit: String
    ): Ingredient {
        return Ingredient(id, recipeId, name, quantity, unit)
    }
}