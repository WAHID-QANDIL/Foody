package org.wahid.foody.data;

import android.app.Application;

import org.wahid.foody.data.local.database.MealRoomDb;
import org.wahid.foody.data.local.database.dao.MealDao;
import org.wahid.foody.data.local.database.entity.MealEntity;
import org.wahid.foody.data.remote.meal_service.RemoteMealDatasource;
import org.wahid.foody.data.remote.meal_service.dto.CategoryRemoteModel;
import org.wahid.foody.data.remote.meal_service.dto.MealDto;
import org.wahid.foody.data.remote.meal_service.dto.RemoteIngredientModel;
import org.wahid.foody.presentation.model.AreaDomainModel;
import org.wahid.foody.presentation.model.CategoryDomainModel;
import org.wahid.foody.presentation.model.IngredientDomainModel;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.presentation.MealRepository;
import org.wahid.foody.utils.ApplicationDependencyRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImpl implements MealRepository {
    private RemoteMealDatasource datasource;
    private MealRoomDb mealRoomDb = ApplicationDependencyRepository.mealRoomDb;
    private MealDao mealDao;


    public MealRepositoryImpl(RemoteMealDatasource datasource) {
        this.datasource = datasource;
        mealDao = mealRoomDb.getMealDao();
    }

    @Override
    public Single<List<MealDomainModel>> getAllMeals(String firstChar) {
        return datasource.getAllMeals(firstChar).map(e->{
            return e.getMeals().stream().map(MealDto::toDomainModel).collect(Collectors.toList());
        });
    }

    @Override
    public Single<List<MealDomainModel>> getMealsByArea(String area) {
        return datasource.getMealByArea(area).map(mealResponse -> {
            return mealResponse.getMeals().stream().map(MealDto::toDomainModel).collect(Collectors.toList());
        });
    }

    @Override
    public Single<List<MealDomainModel>> getMealByMainIngredient(String ingredient) {
        return datasource.getMealByMainIngredient(ingredient).map(mealResponse -> {
            return mealResponse.getMeals().stream().map(MealDto::toDomainModel).collect(Collectors.toList());
        });
    }

    @Override
    public Single<List<MealDomainModel>> filterByMainIngredient(String ingredient) {
        return datasource.filterByMainIngredient(ingredient).map(mealResponse -> {
            return mealResponse.getMeals().stream().map(MealDto::toDomainModel).collect(Collectors.toList());
        });
    }

    @Override
    public Single<MealDomainModel> getRandomMeal() {
        return datasource.getRandomMeal().map(mealResponse ->
        {
           return mealResponse
                           .getMeals()
                           .stream()
                           .map(MealDto::toDomainModel)
                           .collect(Collectors.toList())
                           .get(0);
        });
    }

    @Override
    public Single<List<MealDomainModel>> getMealByCategory(String category) {
        return datasource.getMealByCategory(category).map(
                mealResponse -> {
                    return mealResponse
                            .getMeals()
                            .stream()
                            .map(MealDto::toDomainModel)
                            .collect(Collectors.toList());
                }
        );
    }

    @Override
    public Single<MealDomainModel> getMealByName(String name) {
        return datasource.getMealByName(name).map(
                mealResponse -> {
                    return mealResponse
                            .getMeals()
                            .stream()
                            .map(MealDto::toDomainModel)
                            .collect(Collectors.toList())
                            .get(0);
                }
        );
    }

    @Override
    public Single<MealDomainModel> getMealDetailsById(String id) {
        return datasource.getMealDetailsById(id).map(
                mealResponse -> {
                    return mealResponse
                            .getMeals()
                            .stream()
                            .map(MealDto::toDomainModel)
                            .collect(Collectors.toList())
                            .get(0);
                }
        );
    }

    @Override
    public Single<List<CategoryDomainModel>> getAllCategories() {
        return datasource.getAllCategories().map(response->{
            return response
                    .categories()
                    .stream()
                    .map(CategoryRemoteModel::toCategoryDomainModel)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public Single<List<AreaDomainModel>> getAllAreas(String list) {
        return datasource.getAllAreas(list).map(response->{
            return response
                    .getMeals()
                    .stream()
                    .map(MealDto::toAreaDomainModel)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public Single<List<IngredientDomainModel>> getAllIngredients(String list) {
        return datasource.getAllIngredients(list).map(response->{
            return response
                    .getMeals()
                    .stream()
                    .map(RemoteIngredientModel::toIngredientDomainModel)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public Flowable<List<MealDomainModel>> getAllLocalMeals() {
        return mealDao.getAllMeals().map(mealEntities -> mealEntities.stream().map(MealEntity::toMealDomainModel).collect(Collectors.toList()));
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