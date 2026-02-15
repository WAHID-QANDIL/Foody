package org.wahid.foody.data.meals.remote.dto;

import java.util.List;

public class IngredientRemoteResponse {

    List<RemoteIngredientModel> meals;

    public List<RemoteIngredientModel> getMeals() {
        return meals;
    }

    public void setMeals(List<RemoteIngredientModel> meals) {
        this.meals = meals;
    }
}
