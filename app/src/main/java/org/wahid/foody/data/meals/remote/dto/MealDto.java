package org.wahid.foody.data.meals.remote.dto;

import com.google.gson.annotations.SerializedName;

import org.wahid.foody.domain.model.AreaDomainModel;
import org.wahid.foody.domain.model.MealDomainModel;
import org.wahid.foody.utils.CountryCodeLocalDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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


    public MealDomainModel toDomainModel(){
        return new MealDomainModel(mealId, mealName, category, area, mealImageUrl,mealVideoUrl,sourceUrl,splitInstructions(),ingredients);
    }

    public AreaDomainModel toAreaDomainModel(){

        return new AreaDomainModel(CountryCodeLocalDataSource.getImageUrl(area),area);
    }


    private List<String> splitInstructions(){
        return Arrays.stream(instructions.split("\\.")).map(String::trim).collect(Collectors.toList());
    }




}