package com.bober.recipesapp

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bober.recipesapp.database.AndroidDatabaseDriverFactory
import com.bober.recipesapp.database.Database
import com.bober.recipesapp.entity.Ingredient
import com.bober.recipesapp.entity.Recipe
import kotlinx.coroutines.*

class MyRecipesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Database(AndroidDatabaseDriverFactory(application.applicationContext))

    private val _state = mutableStateOf(RecipesScreenState())
    val state: State<RecipesScreenState> = _state

    private val checkedState = mutableStateMapOf<Long, Boolean>()

    init {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            //database.insertRecipe("Pizza Margherita", "Classic Italian pizza") //test
            //database.insertRecipe("Spaghetti Carbonara", "Traditional Roman dish") //test
            println(">>> init ")
            loadRecipes()
        }
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val recipes = database.getAllRecipes()
                _state.value = _state.value.copy(isLoading = false, recipes = recipes)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, recipes = emptyList())
            }
        }
    }

    fun selectRecipe(recipe: Recipe) {
        _state.value = _state.value.copy(selectedRecipe = recipe)
        loadIngredients(recipe.id)
    }

    fun loadIngredients(recipeId: Long) {
        viewModelScope.launch {
            try {
                val ingredients = database.selectIngredientsByRecipeId(recipeId).map {
                    it.copy(checked = checkedState[it.id] ?: false)
                }
                val recipe = database.selectRecipeById(recipeId)
                _state.value = _state.value.copy(
                    ingredients = ingredients,
                    selectedRecipe = recipe
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    ingredients = emptyList(),
                    selectedRecipe = null
                )
            }
        }
    }

    fun deleteRecipe(recipeId: Long) {
        println(">>> Usuwam przepis o id = $recipeId")
        viewModelScope.launch {
            try {
                database.deleteRecipe(recipeId)
                loadRecipes()
            } catch (e: Exception) {
                println(">>> Błąd przy usuwaniu: ${e.message}")
            }
        }
    }

    fun addRecipe(recipeName: String) {
        viewModelScope.launch {
            try {
                database.insertRecipe(recipeName,"")
                loadRecipes()
            } catch (e: Exception) {
                println(">>> Błąd przy dodawaniu przepisu: ${e.message}")
            }
        }
    }

    fun toggleIngredientChecked(ingredientId: Long) {
        val current = checkedState[ingredientId] ?: false
        checkedState[ingredientId] = !current
        _state.value = _state.value.copy(
            ingredients = _state.value.ingredients.map {
                if (it.id == ingredientId) it.copy(checked = !current) else it.copy(checked = checkedState[it.id] ?: it.checked)
            }
        )
    }

    // do testow
    fun deleteAllRecipes() {
        viewModelScope.launch {
            database.deleteAllRecipes()
            loadRecipes()
        }
    }
}

data class RecipesScreenState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val selectedRecipe: Recipe? = null,
    val ingredients: List<Ingredient> = emptyList(),
)