package org.wahid.foody.presentation.home;

public record MealsRecyclerViewCardItem(
        String mealId,
        String mealTitle,
        String rate,
        String imageUrl
) { }