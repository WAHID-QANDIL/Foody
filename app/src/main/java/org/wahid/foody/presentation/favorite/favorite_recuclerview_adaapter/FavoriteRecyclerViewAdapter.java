package org.wahid.foody.presentation.favorite.favorite_recuclerview_adaapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.wahid.foody.R;
import org.wahid.foody.presentation.model.MealDomainModel;
import org.wahid.foody.utils.CountryCodeLocalDataSource;
import org.wahid.foody.utils.ImageLoader;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter<FavoriteRecyclerViewAdapter.FavoriteRecyclerViewHolder> {

    private Function1<String, Unit> onItemClicked = null;
    private Function1<MealDomainModel, Unit> onRemoveClicked = null;
    private List<MealDomainModel> items;
    private static final String TAG = "FavoriteRecyclerViewAda";

    public FavoriteRecyclerViewAdapter(List<MealDomainModel> items, Function1<String, Unit> onItemClicked ) {
        this.items = items;
        this.onItemClicked = onItemClicked;

    }

    public void setOnRemoveClicked(Function1<MealDomainModel, Unit> onRemoveClicked) {
        this.onRemoveClicked = onRemoveClicked;
    }

    public void setItemsAndUpdate(List<MealDomainModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }






    @NonNull
    @Override
    public FavoriteRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_recipe,parent,false);
        return new FavoriteRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecyclerViewHolder holder, int position) {
        MealDomainModel item = items.get(position);
        Log.d(TAG, "onBindViewHolder: "+ CountryCodeLocalDataSource.getCountryCode(item.area()));

        holder.removeFromFavoriteBtn.setOnClickListener(v -> onRemoveClicked.invoke(item));
        holder.title.setText(item.mealName());
        holder.flagName.setText(CountryCodeLocalDataSource.getCountryCode(item.area()));

        holder.category.setText(item.category());
        holder.imageView.setOnClickListener(v -> onRemoveClicked.invoke(item));
        ImageLoader.load(holder.flagImage,CountryCodeLocalDataSource.getImageUrl(item.area()));
        ImageLoader.load(holder.imageView, item.mealImageUrl());
        Log.d(TAG, "onBindViewHolder: "+ item.mealImageUrl());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class FavoriteRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView flagName;
        TextView category;
        ImageView imageView;
        ImageView flagImage;
        MaterialButton removeFromFavoriteBtn;

        public FavoriteRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivRecipe);
            title = itemView.findViewById(R.id.tvRecipeName);
            flagImage = itemView.findViewById(R.id.ivFlag);
            flagName = itemView.findViewById(R.id.tvCountryCode);
            category = itemView.findViewById(R.id.tvRecipeDetails);
            removeFromFavoriteBtn = itemView.findViewById(R.id.btn_remove_from_favorite);

        }
    }
}