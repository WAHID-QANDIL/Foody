package org.wahid.foody.presentation.plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wahid.foody.R;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PlanMealAdapter extends RecyclerView.Adapter<PlanMealAdapter.MealViewHolder> {

    private List<MealDomainModel> meals = new ArrayList<>();
    private OnMealClickListener clickListener;
    private OnMealDeleteListener deleteListener;

    public interface OnMealClickListener {
        void onMealClick(MealDomainModel meal);
    }

    public interface OnMealDeleteListener {
        void onMealDelete(MealDomainModel meal);
    }

    public PlanMealAdapter(OnMealClickListener onMealClickListener, OnMealDeleteListener deleteListener) {
        this.clickListener = onMealClickListener;
        this.deleteListener = deleteListener;
    }

    public void updateMeals(List<MealDomainModel> newMeals) {
        this.meals.clear();
        this.meals.addAll(newMeals);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_plan_card, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealDomainModel meal = meals.get(position);
        holder.bind(meal);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMeal;
        private final TextView tvMealTitle;
        private final TextView tvPrepTime;
        private final ImageButton btnDetails;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMeal = itemView.findViewById(R.id.ivMeal);
            tvMealTitle = itemView.findViewById(R.id.tvMealTitle);
            tvPrepTime = itemView.findViewById(R.id.tvPrepTime);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }

        public void bind(MealDomainModel meal) {
            tvMealTitle.setText(meal.mealName());
            tvPrepTime.setText(meal.area());
            ImageLoader.load(ivMeal, meal.mealImageUrl());

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onMealClick(meal);
                }
            });

            btnDetails.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onMealClick(meal);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onMealDelete(meal);
                }
                return true;
            });
        }
    }
}