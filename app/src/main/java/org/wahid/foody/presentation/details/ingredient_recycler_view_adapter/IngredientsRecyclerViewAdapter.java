package org.wahid.foody.presentation.details.ingredient_recycler_view_adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.wahid.foody.R;
import org.wahid.foody.utils.ImageLoader;

import java.util.List;

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.IngredientViewHolder> {

    private List<IngredientRecyclerViewModel> ingredients;
    private static final String TAG = "IngredientsRecyclerView";
    private final String INGREDIENT_BASE_URL = "https://www.themealdb.com/images/ingredients/";

    public void updateAndNotifyListItems(List<IngredientRecyclerViewModel> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_recycler_view_item, parent,false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        IngredientRecyclerViewModel model = ingredients.get(position);
        holder.ingredientOrder.setText(String.valueOf(position+1));
        holder.ingredientName.setText(model.ingredientName());
        holder.ingredientMeasure.setText(model.ingredientMeasure());
        String url = INGREDIENT_BASE_URL + model.ingredientName() + ".png";
        ImageLoader.load(holder.imageView,url);
        Log.d(TAG, "onBindViewHolder: "+ url);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder{
        TextView ingredientOrder;
        TextView ingredientName;
        TextView ingredientMeasure;
        ImageView imageView;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View item){
            ingredientName = item.findViewById(R.id.tv_ingredient_name);
            ingredientOrder = item.findViewById(R.id.tv_ingredient_order_number);
            ingredientMeasure = item.findViewById(R.id.tv_ingredient_measure);
            imageView = item.findViewById(R.id.iv_ingredient);
        }
    }
}
