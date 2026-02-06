package org.wahid.foody.presentation.home;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class PopularMealsRecyclerViewAdapter extends RecyclerView.Adapter<PopularMealsRecyclerViewAdapter.MealViewHolder> {




    private List<RecyclerViewCardItem> listItems = new ArrayList<>();
    private Function1<String, Unit> onItemClicked = null;
    private static final String TAG = "PopularMealsRecyclerVie";




    public PopularMealsRecyclerViewAdapter() {
    }

    public List<RecyclerViewCardItem> getListItems() {
        return listItems;
    }

    public void updateListItems(List<RecyclerViewCardItem> listItems){
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
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_meal_custom_card_layout,parent,false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        RecyclerViewCardItem item = listItems.get(position);

        holder.viewRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClicked.invoke(item.mealId());
            }
        });
        Glide.with(holder.imageView.getContext())
                .load(item.imageUrl())
                .into(holder.imageView);
        holder.title.setText(item.mealTitle());

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
