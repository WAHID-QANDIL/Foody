package org.wahid.foody.presentation.model;

import org.wahid.foody.data.remote.meal_service.dto.Ingredient;

import java.util.List;

public record MealDomainModel(
    String mealId,
    String mealName,
    String category,
    String area,
    String mealImageUrl,
    String mealVideoUrl,
    String sourceUrl,
    List<String> instructions,
    List<Ingredient> ingredients
) {
}
