package org.wahid.foody.presentation.search_details;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.wahid.foody.R;

public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialogFragment";
    public static final String REQUEST_KEY = "filter_request";
    public static final String RESULT_KEY = "filter_result";

    private static final String ARG_CATEGORIES = "categories";
    private static final String ARG_COUNTRIES = "countries";
    private static final String ARG_INGREDIENTS = "ingredients";
    private static final String ARG_CURRENT_CATEGORY = "current_category";
    private static final String ARG_CURRENT_COUNTRY = "current_country";
    private static final String ARG_CURRENT_INGREDIENT = "current_ingredient";

    public static FilterDialogFragment newInstance(
            String[] categories,
            String[] countries,
            String[] ingredients,
            @Nullable SearchFilterQuery currentQuery
    ) {
        FilterDialogFragment fragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_CATEGORIES, categories);
        args.putStringArray(ARG_COUNTRIES, countries);
        args.putStringArray(ARG_INGREDIENTS, ingredients);

        if (currentQuery != null) {
            args.putString(ARG_CURRENT_CATEGORY, currentQuery.category);
            args.putString(ARG_CURRENT_COUNTRY, currentQuery.country);
            args.putString(ARG_CURRENT_INGREDIENT, currentQuery.ingredient);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        AutoCompleteTextView etCategory = view.findViewById(R.id.etCategory);
        AutoCompleteTextView etCountry = view.findViewById(R.id.etCountry);
        AutoCompleteTextView etIngredient = view.findViewById(R.id.etIngredient);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] categories = arguments.getStringArray(ARG_CATEGORIES);
            String[] countries = arguments.getStringArray(ARG_COUNTRIES);
            String[] ingredients = arguments.getStringArray(ARG_INGREDIENTS);

            if (categories != null) {
                etCategory.setAdapter(new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        categories
                ));
            }

            if (countries != null) {
                etCountry.setAdapter(new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        countries
                ));
            }

            if (ingredients != null) {
                etIngredient.setAdapter(new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        ingredients
                ));
            }

            // Restore current selection if available
            String currentCategory = arguments.getString(ARG_CURRENT_CATEGORY);
            String currentCountry = arguments.getString(ARG_CURRENT_COUNTRY);
            String currentIngredient = arguments.getString(ARG_CURRENT_INGREDIENT);

            if (currentCategory != null) {
                etCategory.setText(currentCategory, false);
            }
            if (currentCountry != null) {
                etCountry.setText(currentCountry, false);
            }
            if (currentIngredient != null) {
                etIngredient.setText(currentIngredient, false);
            }
        }

        return new AlertDialog.Builder(requireContext())
                .setTitle("Filter Search")
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Clear", (dialog, which) -> {
                    // Return empty filter to clear all filters
                    SearchFilterQuery result = new SearchFilterQuery(null, null, null);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RESULT_KEY, result);
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, bundle);
                })
                .setPositiveButton("Apply", (dialog, which) -> {
                    String category = etCategory.getText().toString().trim();
                    String country = etCountry.getText().toString().trim();
                    String ingredient = etIngredient.getText().toString().trim();

                    SearchFilterQuery result = new SearchFilterQuery(
                            category.isEmpty() ? null : category,
                            country.isEmpty() ? null : country,
                            ingredient.isEmpty() ? null : ingredient
                    );

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RESULT_KEY, result);
                    getParentFragmentManager().setFragmentResult(REQUEST_KEY, bundle);
                })
                .create();
    }
}

