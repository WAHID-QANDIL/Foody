package org.wahid.foody.data;

import org.wahid.foody.data.meals.MealDatasource;
import org.wahid.foody.data.meals.local.database.MealRoomDb;
import org.wahid.foody.data.meals.local.database.dao.MealDao;
import org.wahid.foody.data.meals.local.database.entity.MealEntity;
import org.wahid.foody.data.meals.remote.dto.CategoryRemoteModel;
import org.wahid.foody.data.meals.remote.dto.MealDto;
import org.wahid.foody.data.meals.remote.dto.RemoteIngredientModel;
import org.wahid.foody.domain.model.AreaDomainModel;
import org.wahid.foody.domain.model.CategoryDomainModel;
import org.wahid.foody.domain.model.IngredientDomainModel;
import org.wahid.foody.domain.model.MealDomainModel;
import org.wahid.foody.domain.repository.MealRepository;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImpl implements MealRepository {
    private MealDatasource datasource;
    private MealDao mealDao;


    public MealRepositoryImpl(MealDatasource datasource, MealRoomDb mealRoomDb) {
        this.mealDao = mealRoomDb.getMealDao();
        this.datasource = datasource;
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


    //Local

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