package com.dicoding.lecker

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.lecker.databinding.ActivityRecipeDetailsBinding

class RecipeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipeTitle = intent.getStringExtra("RECIPE_TITLE")
        val recipeIngredients = intent.getStringExtra("RECIPE_INGREDIENTS")
        val recipeSteps = intent.getStringExtra("RECIPE_STEPS")

        val formattedIngredients = recipeIngredients?.let { formatIngredients(it) }
        val formattedSteps = recipeSteps?.let { formatSteps(it) }

        binding.tvRecipeTitle.text = recipeTitle
        binding.tvRecipeIngredients.text = formattedIngredients
        binding.tvRecipeSteps.text = formattedSteps
    }

    private fun formatIngredients(ingredients: String): Spanned {
        val formattedIngredients = ingredients.replace("--", "<br>&bull; ")
            .removeSuffix("<br>&bull; ")
            .prependIndent("<br>&bull; ")
        return fromHtml(formattedIngredients)
    }

    private fun formatSteps(steps: String): Spanned {
        val replacedText = steps.replace("--", ".")
        val formattedSteps = replacedText.split(".")
            .filter { it.isNotBlank() }
            .mapIndexed { index, step ->
                val trimmedStep = step.trim()
                val formattedStep = "${index + 1}. ${trimmedStep}"
                if (index > 0) {
                    val firstChar = formattedStep.indexOfFirst { it.isLetter() }
                    if (firstChar >= 0) {
                        formattedStep.replaceRange(
                            firstChar,
                            firstChar + 1,
                            formattedStep[firstChar].uppercase()
                        )
                    } else {
                        formattedStep
                    }
                } else {
                    formattedStep
                }
            }
            .joinToString("<br>")
        return fromHtml(formattedSteps)
    }

    @Suppress("DEPRECATION")
    private fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }
}
