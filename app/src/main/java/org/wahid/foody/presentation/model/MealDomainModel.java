package org.wahid.foody.presentation.model;

import static org.wahid.foody.utils.Constants.DELIMITER;

import com.google.firebase.firestore.DocumentSnapshot;

import org.wahid.foody.data.local.database.entity.MealEntity;
import org.wahid.foody.data.remote.meal_service.dto.Ingredient;
import org.wahid.foody.data.remote.user_auth.firebase.FirebaseClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record MealDomainModel(
    String mealId,
    String mealName,
    String category,
    String area,
    String mealImageUrl,
    String mealVideoUrl,
    String sourceUrl,
    List<String> instructions,
    List<Ingredient> ingredients
) {

    private static final String MEAL_ID = "id";
    private static final String MEAL_NAME = "name";
    private static final String MEAL_CATEGORY = "category";
    private static final String MEAL_AREA = "area";
    private static final String MEAL_IMAGE_URL = "imageUrl";

    public MealEntity toDatabaseEntity(){
        String userId = Objects.requireNonNull(
                FirebaseClient.getInstance().getCurrentUser(),
                "User must be logged in to save meals"
        ).getUid();
        return new MealEntity(
                mealId,
                userId,
                mealName,
                category,
                area,
                instructions.stream().map(it-> it+DELIMITER).collect(Collectors.joining()),
                mealImageUrl,
                mealVideoUrl,
                sourceUrl,
                ingredients
        );
    }

    public Map<String, Object> toFirestoreDocument(){
        Map<String, Object> doc = new HashMap<>();
        doc.put(MEAL_ID, mealId);
        doc.put(MEAL_NAME, mealName);
        doc.put(MEAL_CATEGORY, category);
        doc.put(MEAL_AREA, area);
        doc.put(MEAL_IMAGE_URL, mealImageUrl);
        return doc;
    }

    public static MealDomainModel fromFirestoreDocument(DocumentSnapshot document) {
        if (document == null || !document.exists()) {
            return null;
        }

        String id = document.getString(MEAL_ID);
        String name = document.getString(MEAL_NAME);
        String category = document.getString(MEAL_CATEGORY);
        String area = document.getString(MEAL_AREA);
        String imageUrl = document.getString(MEAL_IMAGE_URL);

        return new MealDomainModel(
                id != null ? id : "",
                name != null ? name : "",
                category != null ? category : "",
                area != null ? area : "",
                imageUrl != null ? imageUrl : "",
                "",  // videoUrl not stored in plan
                "",  // sourceUrl not stored in plan
                new ArrayList<>(),  // instructions not stored in plan
                new ArrayList<>()   // ingredients not stored in plan
        );
    }
}
