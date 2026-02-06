package org.wahid.foody.presentation.home;

import org.wahid.foody.presentation.BasePresenter;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;

public interface HomeView extends BasePresenter {
    void bindRandomMealIntoCard(MealDomainModel meal);
    void bindPopularMealsIntoRecyclerView(List<MealDomainModel> models);

}
