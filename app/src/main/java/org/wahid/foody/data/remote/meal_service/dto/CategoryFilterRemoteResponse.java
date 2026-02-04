package org.wahid.foody.data.remote.meal_service.dto;

import java.util.List;

public class CategoryFilterRemoteResponse {

    private List<CategoryMeal> meals;

    public List<CategoryMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<CategoryMeal> meals) {
        this.meals = meals;
    }
}
