package org.wahid.foody.data.local.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wahid.foody.data.remote.meal_service.dto.Ingredient;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromListToString(List<Ingredient> ingredients){
       Gson converter = new Gson();

        if (ingredients == null)return converter.toString();
        return new Gson().toJson(ingredients);
    }
    @TypeConverter
    public List<Ingredient> fromStringToList(String json){
        if (json == null) return null;
        Type typeToken = new TypeToken<>(){}.getType();
        return new Gson().fromJson(json,typeToken);
    }
}