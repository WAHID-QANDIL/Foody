package org.wahid.foody.data.local.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.wahid.foody.data.local.database.dao.MealDao;
import org.wahid.foody.data.local.database.entity.MealEntity;

import kotlin.jvm.Volatile;

@Database(entities = {MealEntity.class} ,version = 1)
@TypeConverters(value = Converters.class)
public abstract class MealRoomDb extends RoomDatabase {

    abstract public MealDao getMealDao();
    @Volatile
    private static MealRoomDb INSTANCE = null;

    public static synchronized MealRoomDb  getInstance(Application ctx){

        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(ctx, MealRoomDb.class, "mealDb").build();
        }

        return INSTANCE;
    }


}
