// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package org.wahid.foody.domain.remote;
import java.util.List;
import java.util.Map;

public class MealResponse {
    private List<Map<String, String>> meals;

    public List<Map<String, String>> getMeals() { return meals; }
    public void setMeals(List<Map<String, String>> value) { this.meals = value; }
}