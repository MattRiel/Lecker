package com.dicoding.lecker

data class RecipeData(
    val result: List<Recipe>
)

data class Recipe(
    val title: String,
    val ingredients: String,
    val steps: String
)
