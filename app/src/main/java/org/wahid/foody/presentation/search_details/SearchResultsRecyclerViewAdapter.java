package org.wahid.foody.presentation.search_details;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import org.wahid.foody.R;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.MealViewHolder> {


    private List<MealDomainModel> listItems = new ArrayList<>();
    private Function1<String, Unit> onItemClicked = null;
    private static final String TAG = "PopularMealsRecyclerVie";


    public List<MealDomainModel> getListItems() {
        return listItems;
    }

    public void updateListItems(List<MealDomainModel> listItems){
        this.listItems = listItems;
        Log.d(TAG, "updateListItems: " + listItems);
        notifyDataSetChanged();
    }

    public Function1<String, Unit> getOnItemClicked() {
        return onItemClicked;
    }

    public void setOnItemClicked(Function1<String, Unit> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public SearchResultsRecyclerViewAdapter.MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item_card,parent,false);
        return new SearchResultsRecyclerViewAdapter.MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        MealDomainModel item = listItems.get(position);
        Log.d(TAG, "onBindViewHolder: "+ item.mealImageUrl());
        Glide.with(holder.imageView.getContext())
                .load(item.mealImageUrl())
                .into(holder.imageView);
        holder.title.setText(item.mealName());
        holder.viewRecipeButton.setOnClickListener(v -> onItemClicked.invoke(item.mealId()));
        holder.imageView.setOnClickListener(v -> onItemClicked.invoke(item.mealId()));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView imageView;
        MaterialButton viewRecipeButton;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            bind(itemView);
        }


        void bind(View item){
            title = item.findViewById(R.id.txt_meal_name);
            imageView = item.findViewById(R.id.img_meal);
            viewRecipeButton = item.findViewById(R.id.btn_view_recipe);
        }


    }

}
