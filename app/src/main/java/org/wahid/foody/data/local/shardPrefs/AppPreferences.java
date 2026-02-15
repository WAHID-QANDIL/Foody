package org.wahid.foody.data.local.shardPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

public class AppPreferences {
    private static final String PREF_NAME = "foody_preferences";
    private static final String KEY_FIRST_LAUNCH = "is_first_launch";
    private static final String KEY_MEAL_OF_THE_DAY_ID = "meal_of_the_day_id";
    private static final String KEY_MEAL_OF_THE_DAY_DATE = "meal_of_the_day_date";

    private final SharedPreferences sharedPreferences;

    private static AppPreferences instance;

    private AppPreferences(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized AppPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new AppPreferences(context);
        }
        return instance;
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunchCompleted() {
        sharedPreferences.edit()
                .putBoolean(KEY_FIRST_LAUNCH, false)
                .apply();
    }

    public void resetFirstLaunch() {
        sharedPreferences.edit()
                .putBoolean(KEY_FIRST_LAUNCH, true)
                .apply();
    }

    public String getMealOfTheDayId() {
        String savedDate = sharedPreferences.getString(KEY_MEAL_OF_THE_DAY_DATE, null);
        String todayDate = LocalDate.now().toString();

        if (savedDate != null && savedDate.equals(todayDate)) {
            return sharedPreferences.getString(KEY_MEAL_OF_THE_DAY_ID, null);
        }
        return null;
    }


    public void setMealOfTheDayId(String mealId) {
        String todayDate = LocalDate.now().toString();
        sharedPreferences.edit()
                .putString(KEY_MEAL_OF_THE_DAY_ID, mealId)
                .putString(KEY_MEAL_OF_THE_DAY_DATE, todayDate)
                .apply();
    }

    public void clearMealOfTheDay() {
        sharedPreferences.edit()
                .remove(KEY_MEAL_OF_THE_DAY_ID)
                .remove(KEY_MEAL_OF_THE_DAY_DATE)
                .apply();
    }
}

