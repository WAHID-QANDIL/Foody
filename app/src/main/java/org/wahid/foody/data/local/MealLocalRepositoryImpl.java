package org.wahid.foody.data.local;

import org.wahid.foody.data.local.database.MealRoomDb;
import org.wahid.foody.data.local.database.dao.MealDao;
import org.wahid.foody.data.local.database.entity.MealEntity;
import org.wahid.foody.presentation.MealLocalRepository;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public class MealLocalRepositoryImpl implements MealLocalRepository {
    private MealDao mealDao;

    public MealLocalRepositoryImpl(MealRoomDb mealRoomDb) {
        mealDao =  mealRoomDb.getMealDao();
    }

    @Override
    public Flowable<List<MealDomainModel>> getAllLocalMeals(String userId) {
        return mealDao.getAllMeals(userId).map(mealEntities -> mealEntities.stream().map(MealEntity::toMealDomainModel).collect(Collectors.toList()));
    }

    @Override
    public Completable insertANewMeal(MealDomainModel meal) {
        return mealDao.insertANewMeal(meal.toDatabaseEntity());
    }

    @Override
    public Completable deleteMealById(MealDomainModel meal) {
        return mealDao.deleteMeal(meal.toDatabaseEntity());
    }
}