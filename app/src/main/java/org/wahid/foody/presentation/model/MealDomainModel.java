package org.wahid.foody.presentation.model;

import static org.wahid.foody.utils.Constants.DELIMITER;

import org.wahid.foody.data.local.database.entity.MealEntity;
import org.wahid.foody.data.remote.meal_service.dto.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

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

    public MealEntity toDatabaseEntity(){
        return new MealEntity(
                mealId,
                mealName,
                category,
                area,
                mealImageUrl,
                mealVideoUrl,
                sourceUrl,
                instructions.stream().map(it-> it+DELIMITER).collect(Collectors.joining()),
                ingredients
        );
    }
}
