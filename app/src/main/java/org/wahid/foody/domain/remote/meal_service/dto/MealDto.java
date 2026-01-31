package org.wahid.foody.domain.remote.meal_service.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record MealDto(
        @SerializedName("idMeal")
        String mealId,
        @SerializedName("strMeal")
        String mealName,
        @SerializedName("strCategory")
        String category,
        @SerializedName("strArea")
        String area,
        @SerializedName("strInstructions")
        String instructions,
        @SerializedName("strMealThumb")
        String mealImageUrl,
        @SerializedName("strYoutube")
        String mealVideoUrl,
        @SerializedName("strSource")
        String sourceUrl,
        List<Ingredient> ingredients
) { }