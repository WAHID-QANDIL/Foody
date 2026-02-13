package org.wahid.foody.data.remote.meal_service.dto;

import org.wahid.foody.presentation.details.ingredient_recycler_view_adapter.IngredientRecyclerViewModel;

public record Ingredient(
        String ingredientName,
        String ingredientMeasure
) {

    public IngredientRecyclerViewModel toRecyclerViewItem(){
        return new IngredientRecyclerViewModel(ingredientName,ingredientMeasure);
    }

}