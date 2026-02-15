package org.wahid.foody.utils;

import android.app.Application;

import org.wahid.foody.FoodyApplication;
import org.wahid.foody.data.MealRepositoryImpl;
import org.wahid.foody.data.meals.local.database.MealRoomDb;
import org.wahid.foody.data.meals.FirestoreRepositoryImpl;
import org.wahid.foody.data.meals.RemoteMealDatasource;
import org.wahid.foody.domain.repository.FirestoreRepository;
import org.wahid.foody.domain.repository.MealRepository;

import kotlin.jvm.Volatile;

public abstract class ApplicationDependencyRepository {

    public @Volatile static Application application = FoodyApplication.application;
    public @Volatile static MealRoomDb mealRoomDb = MealRoomDb.getInstance(application);
    public @Volatile static RemoteMealDatasource remoteMealDatasource = new RemoteMealDatasource();
    public @Volatile static MealRepository mealRepository = new MealRepositoryImpl(remoteMealDatasource,mealRoomDb);
    public @Volatile static FirestoreRepository firestoreRepository = new FirestoreRepositoryImpl();


}