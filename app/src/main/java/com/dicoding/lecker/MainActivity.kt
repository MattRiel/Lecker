package com.dicoding.lecker

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.lecker.databinding.ActivityMainBinding
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipeAdapter = RecipeAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.recycler_view_spacing)
        binding.recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPixels))

        binding.recyclerView.adapter = recipeAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://leckerscapstone12-gjlu4leqjq-uc.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        binding.btnSend.setOnClickListener {
            animateButton(binding.btnSend)
            val inputText = binding.editText.text.toString()

            val json = "{\"Ingredient\":\"$inputText\"}"

            val requestBody = RequestBody.create(MediaType.parse("application/json"), json)

            apiService.createPost(requestBody).enqueue(object : Callback<RecipeData> {
                override fun onResponse(call: Call<RecipeData>, response: Response<RecipeData>) {
                    if (response.isSuccessful) {
                        val recipeData = response.body()
                        if (recipeData != null) {
                            recipeAdapter.updateData(recipeData.result)
                        }
                    } else {
                        Toast.makeText(applicationContext, "Failed to retrieve response data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RecipeData>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failed to retrieve recipe data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun animateButton(button: View) {
        val scaleX = ObjectAnimator.ofFloat(button, View.SCALE_X, 1f, 0.9f, 1f)
        val scaleY = ObjectAnimator.ofFloat(button, View.SCALE_Y, 1f, 0.9f, 1f)
        val alpha = ObjectAnimator.ofFloat(button, View.ALPHA, 1f, 0.5f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.duration = 1000
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.start()
    }
}
