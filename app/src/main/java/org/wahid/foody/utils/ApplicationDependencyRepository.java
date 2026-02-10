package org.wahid.foody.utils;

import android.app.Application;

import org.wahid.foody.FoodyApplication;
import org.wahid.foody.data.MealRepositoryImpl;
import org.wahid.foody.data.local.database.MealRoomDb;
import org.wahid.foody.data.remote.meal_service.RemoteMealDatasource;
import org.wahid.foody.presentation.MealRepository;

import kotlin.jvm.Volatile;

public abstract class ApplicationDependencyRepository {

    public @Volatile static Application application = FoodyApplication.application;
    public static RemoteMealDatasource remoteMealDatasource = new RemoteMealDatasource();
    public static MealRepository repository = new MealRepositoryImpl(remoteMealDatasource);
    public static MealRoomDb mealRoomDb = MealRoomDb.getInstance(application);

}
