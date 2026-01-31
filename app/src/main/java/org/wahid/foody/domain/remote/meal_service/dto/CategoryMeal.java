package org.wahid.foody.domain.remote.meal_service.dto;

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
