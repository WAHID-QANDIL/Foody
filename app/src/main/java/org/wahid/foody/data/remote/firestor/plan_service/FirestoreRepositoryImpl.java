package org.wahid.foody.data.remote.firestor.plan_service;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.wahid.foody.presentation.FirestoreRepository;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirestoreRepositoryImpl implements FirestoreRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "FirestoreRepositoryImpl";

    private String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
    }

    @Nullable
    private CollectionReference getMealCollectionRef(String date, String mealType) {
        String currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            return db.collection("userMeals")
                    .document(currentUserId)
                    .collection("dailyMeals")
                    .document(date)
                    .collection(mealType);
        } else {
            Log.d(TAG, "getMealCollectionRef: User not authenticated");
            return null;
        }
    }

    @Override
    public Completable addANewDaySlotMeal(String date, MealDomainModel meal, String mealType) {
        return Completable.create(emitter -> {
            CollectionReference mealRef = getMealCollectionRef(date, mealType);
            if (mealRef != null) {
                Map<String, Object> doc = meal.toFirestoreDocument();
                mealRef.document(meal.mealId())
                        .set(doc)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "addANewDaySlotMeal: Meal added successfully");
                            emitter.onComplete();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "addANewDaySlotMeal: Failed to add meal", e);
                            emitter.onError(e);
                        });
            } else {
                emitter.onError(new Exception("User not authenticated"));
            }
        });
    }

    @Override
    public Completable removeADaySlotMeal(String date, String mealId, String mealType) {
        return Completable.create(emitter -> {
            CollectionReference mealRef = getMealCollectionRef(date, mealType);
            if (mealRef != null) {
                mealRef.document(mealId)
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "removeADaySlotMeal: Meal removed successfully");
                            emitter.onComplete();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "removeADaySlotMeal: Failed to remove meal", e);
                            emitter.onError(e);
                        });
            } else {
                emitter.onError(new Exception("User not authenticated"));
            }
        });
    }

    @Override
    public Single<List<MealDomainModel>> getADaySlotMeals(String date, String mealType) {
        return Single.create(emitter -> {
            CollectionReference mealRef = getMealCollectionRef(date, mealType);
            if (mealRef != null) {
                mealRef.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<MealDomainModel> meals = new ArrayList<>();
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                MealDomainModel meal = MealDomainModel.fromFirestoreDocument(doc);
                                if (meal != null) {
                                    meals.add(meal);
                                }
                            }
                            Log.d(TAG, "getADaySlotMeals: Retrieved " + meals.size() + " meals");
                            emitter.onSuccess(meals);
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "getADaySlotMeals: Failed to get meals", e);
                            emitter.onError(e);
                        });
            } else {
                emitter.onError(new Exception("User not authenticated"));
            }
        });
    }

    @Override
    public Single<List<MealDomainModel>> getAllMealsForDate(String date) {
        return Single.create(emitter -> {
            String currentUserId = getCurrentUserId();
            if (currentUserId == null) {
                emitter.onError(new Exception("User not authenticated"));
                return;
            }

            List<MealDomainModel> allMeals = new ArrayList<>();
            String[] mealTypes = {"Breakfast", "Lunch", "Dinner"};
            final int[] completedQueries = {0};

            for (String mealType : mealTypes) {
                CollectionReference mealRef = getMealCollectionRef(date, mealType);
                if (mealRef != null) {
                    mealRef.get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                    MealDomainModel meal = MealDomainModel.fromFirestoreDocument(doc);
                                    if (meal != null) {
                                        allMeals.add(meal);
                                    }
                                }
                                completedQueries[0]++;
                                if (completedQueries[0] == mealTypes.length) {
                                    Log.d(TAG, "getAllMealsForDate: Retrieved " + allMeals.size() + " meals");
                                    emitter.onSuccess(allMeals);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "getAllMealsForDate: Failed to get meals for " + mealType, e);
                                emitter.onError(e);
                            });
                }
            }
        });
    }

    @Nullable
    private CollectionReference getFavoritesCollectionRef() {
        String currentUserId = getCurrentUserId();
        if (currentUserId != null) {
            return db.collection("userMeals")
                    .document(currentUserId)
                    .collection("favorites");
        } else {
            Log.d(TAG, "getFavoritesCollectionRef: User not authenticated");
            return null;
        }
    }

    @Override
    public Completable syncFavoriteMeals(List<MealDomainModel> meals) {
        return Completable.create(emitter -> {
            CollectionReference favoritesRef = getFavoritesCollectionRef();
            if (favoritesRef != null) {
                // First, delete all existing favorites to ensure a clean overwrite
                favoritesRef.get()
                        .addOnSuccessListener(querySnapshot -> {
                            final int existingDocsCount = querySnapshot.size();

                            if (existingDocsCount == 0) {
                                uploadNewFavorites(favoritesRef, meals, emitter);
                            } else {

                                final int[] deletedCount = {0};
                                final boolean[] deleteError = {false};

                                for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                    doc.getReference().delete()
                                            .addOnSuccessListener(aVoid -> {
                                                deletedCount[0]++;
                                                if (deletedCount[0] == existingDocsCount && !deleteError[0]) {
                                                    Log.d(TAG, "syncFavoriteMeals: Cleared " + existingDocsCount + " existing favorites");
                                                    // Now upload the new meals
                                                    uploadNewFavorites(favoritesRef, meals, emitter);
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                if (!deleteError[0]) {
                                                    deleteError[0] = true;
                                                    Log.e(TAG, "syncFavoriteMeals: Failed to delete existing meal", e);
                                                    emitter.onError(e);
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "syncFavoriteMeals: Failed to get existing favorites", e);
                            emitter.onError(e);
                        });
            } else {
                emitter.onError(new Exception("User not authenticated"));
            }
        });
    }

    private void uploadNewFavorites(CollectionReference favoritesRef, List<MealDomainModel> meals, io.reactivex.rxjava3.core.CompletableEmitter emitter) {
        if (meals.isEmpty()) {
            Log.d(TAG, "uploadNewFavorites: No meals to sync, favorites cleared");
            emitter.onComplete();
            return;
        }

        final int[] completedOps = {0};
        final boolean[] hasError = {false};

        for (MealDomainModel meal : meals) {
            Map<String, Object> doc = meal.toFirestoreDocument();
            favoritesRef.document(meal.mealId())
                    .set(doc)
                    .addOnSuccessListener(aVoid -> {
                        completedOps[0]++;
                        if (completedOps[0] == meals.size() && !hasError[0]) {
                            Log.d(TAG, "uploadNewFavorites: All " + meals.size() + " meals synced successfully");
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!hasError[0]) {
                            hasError[0] = true;
                            Log.e(TAG, "uploadNewFavorites: Failed to sync meal", e);
                            emitter.onError(e);
                        }
                    });
        }
    }

    @Override
    public Single<List<MealDomainModel>> getFavoriteMeals() {
        return Single.create(emitter -> {
            CollectionReference favoritesRef = getFavoritesCollectionRef();
            if (favoritesRef != null) {
                favoritesRef.get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<MealDomainModel> meals = new ArrayList<>();
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                MealDomainModel meal = MealDomainModel.fromFirestoreDocument(doc);
                                if (meal != null) {
                                    meals.add(meal);
                                }
                            }
                            Log.d(TAG, "getFavoriteMeals: Retrieved " + meals.size() + " favorites from Firestore");
                            emitter.onSuccess(meals);
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "getFavoriteMeals: Failed to get favorites", e);
                            emitter.onError(e);
                        });
            } else {
                emitter.onError(new Exception("User not authenticated"));
            }
        });
    }
}