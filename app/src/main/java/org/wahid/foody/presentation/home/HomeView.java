package org.wahid.foody.presentation.home;

import org.wahid.foody.presentation.BaseView;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;

public interface HomeView extends BaseView {
    void bindRandomMealIntoCard(MealDomainModel meal);
    void bindPopularMealsIntoRecyclerView(List<MealDomainModel> models);

}
