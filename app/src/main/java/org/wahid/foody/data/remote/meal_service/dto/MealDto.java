package org.wahid.foody.data.remote.meal_service.dto;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.Arrays;
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
) {

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public MealDomainModel toDomainModel(){
        return new MealDomainModel(mealId, mealName, category, area, mealImageUrl,mealVideoUrl,sourceUrl,splitInstructions(),ingredients);
    }


    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private List<String> splitInstructions(){
        return Arrays.stream(instructions.split("\\.")).map(String::trim).toList();
    }




}