package org.wahid.foody.data.local.database.entity;


import static org.wahid.foody.utils.Constants.DELIMITER;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.wahid.foody.data.remote.meal_service.dto.Ingredient;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity(tableName = "mealTable", primaryKeys = {"id", "userId"})
public record MealEntity(
    @NonNull
    String id,
    @NonNull
    @ColumnInfo(name = "userId")
    String userId,
    @ColumnInfo(name = "name")
    String name,
    @ColumnInfo(name = "category")
    String category,
    @ColumnInfo(name = "area")
    String area,
    @ColumnInfo(name = "instructions")
    String instructions,
    @ColumnInfo(name = "imageUrl")
    String mealImageUrl,
    @ColumnInfo(name = "videoUrl")
    String mealVideoUrl,
    @ColumnInfo(name = "sourceUrl")
    String sourceUrl,
    @ColumnInfo(name = "ingredientUrl")
    List<Ingredient> ingredientList
) {

    public MealDomainModel toMealDomainModel(){
        return new MealDomainModel(
                id,
                name,
                category,
                area,
                mealImageUrl,
                mealVideoUrl,
                sourceUrl,
                Arrays.stream(instructions.split(DELIMITER)).collect(Collectors.toList()),
                ingredientList
        );
    }
}