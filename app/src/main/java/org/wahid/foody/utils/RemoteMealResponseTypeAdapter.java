package org.wahid.foody.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.wahid.foody.data.meals.remote.dto.Ingredient;
import org.wahid.foody.data.meals.remote.dto.MealDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoteMealResponseTypeAdapter extends TypeAdapter<MealDto> {
    private final int INGREDIENT_SLOTS = 20;


    @Override
    public void write(JsonWriter out, MealDto value) throws IOException {
        return;
    }

    @Override
    public MealDto read(JsonReader in) throws IOException {
        JsonElement element = JsonParser.parseReader(in);

        if (element == null || element.isJsonNull()){
            return  null;
        }

        JsonObject obj = element.getAsJsonObject();


        String mealId       = safeTrim(getAsStringSafe(obj,"idMeal"));
        String strMeal      = safeTrim(getAsStringSafe(obj, "strMeal"));
        String category     = safeTrim(getAsStringSafe(obj, "strCategory"));
        String area         = safeTrim(getAsStringSafe(obj, "strArea"));
        String instructions = safeTrim(getAsStringSafe(obj, "strInstructions"));
        String mealThumb    = safeTrim(getAsStringSafe(obj, "strMealThumb"));
        String youtube      = safeTrim(getAsStringSafe(obj, "strYoutube"));
        String source       = safeTrim(getAsStringSafe(obj, "strSource"));


        List<Ingredient> ingredients = new ArrayList<>(INGREDIENT_SLOTS);

        for (int i = 0; i < INGREDIENT_SLOTS; i++) {
            String ingredientKey = "strIngredient" + i;
            String measureKey = "strMeasure" + i;

            String name = getAsStringSafe(obj, ingredientKey);
            String measure = getAsStringSafe(obj, measureKey);

            name = safeTrim(name);
            measure = safeTrim(measure);



            if (!name.isEmpty()) {

                ingredients.add(new Ingredient(name, measure));
            }

        }
        return new MealDto(
                mealId,
                strMeal,
                category,
                area,
                instructions,
                mealThumb,
                youtube,
                source,
                ingredients
        );
    }
    private static String getAsStringSafe(JsonObject obj, String memberName) {
        JsonElement e = obj.get(memberName);
        if (e == null || e.isJsonNull()) return null;
        return e.getAsString();
    }
    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
