package org.wahid.foody.data.remote.meal_service.dto;

import com.google.gson.annotations.SerializedName;

public record CategoryMeal(
        @SerializedName("strMeal")
        String mealName,
        @SerializedName("strMealThumb")
        String mealImageUrl,
        @SerializedName("idMeal")
        String mealId
) {
}
