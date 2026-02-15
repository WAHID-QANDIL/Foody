// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package org.wahid.foody.data.meals.remote.dto;

import java.util.List;

public class MealResponse {
    private List<MealDto> meals;

    public List<MealDto> getMeals() { return meals; }
    public void setMeals(List<MealDto> value) { this.meals = value; }
}