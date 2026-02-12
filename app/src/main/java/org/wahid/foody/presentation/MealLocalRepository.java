package org.wahid.foody.presentation;

import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface MealLocalRepository {

    // Local
    Flowable<List<MealDomainModel>> getAllLocalMeals();
    Completable insertANewMeal(MealDomainModel meal);
    Completable deleteMealById(MealDomainModel meal);
}
