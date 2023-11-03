package com.dicoding.lecker

import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.lecker.databinding.ItemRecipeBinding

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var recipeList: List<Recipe> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    fun updateData(recipes: List<Recipe>) {
        recipeList = recipes
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            binding.tvIngredients.text = formatIngredients(recipe.ingredients)
            binding.tvSteps.text = formatSteps(recipe.steps)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, RecipeDetailsActivity::class.java).apply {
                    putExtra("RECIPE_TITLE", recipe.title)
                    putExtra("RECIPE_INGREDIENTS", recipe.ingredients)
                    putExtra("RECIPE_STEPS", recipe.steps)
                }
                context.startActivity(intent)
            }

        }

        private fun formatIngredients(ingredients: String): Spanned {
            val formattedIngredients = ingredients.replace("--", "<br>&bull; ")
                .removeSuffix("<br>&bull; ")
                .prependIndent("<br>&bull; ")
            return fromHtml(formattedIngredients)
        }

        private fun formatSteps(steps: String): Spanned {
            val replacedText = steps.replace("--", ".")
            val stepsList = replacedText.split(".").filter { it.isNotBlank() }
            val formattedSteps = StringBuilder()
            for (i in stepsList.indices) {
                val step = stepsList[i].trim()
                val formattedStep = "${i + 1}. ${step}"
                val upperCasedStep = formattedStep.replaceFirstChar { it.uppercase() }
                formattedSteps.append(upperCasedStep).append("<br>")
            }
            return fromHtml(formattedSteps.toString())
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
}
