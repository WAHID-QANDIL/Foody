package org.wahid.foody.data.local.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.wahid.foody.data.local.database.entity.MealEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MealDao {


    @Query("Select * from mealTable")
    Flowable<List<MealEntity>> getAllMeals();

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MealEntity.class)
    Completable insertANewMeal(MealEntity entity);


    @Delete(entity = MealEntity.class)
    Completable deleteMeal(MealEntity entity);

}