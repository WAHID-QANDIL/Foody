package org.wahid.foody.presentation.search.category_list_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.wahid.foody.R;
import org.wahid.foody.presentation.search.OnSearchScreenListItemClick;
import org.wahid.foody.utils.ImageLoader;

import java.util.Collections;
import java.util.List;

public class CategoriesRecyclerViewListAdapter extends RecyclerView.Adapter<CategoriesRecyclerViewListAdapter.CategoryViewHolder> {

    private List<CategoryRecyclerViewListItem> items = Collections.emptyList();
    private OnSearchScreenListItemClick clickListener;

    public CategoriesRecyclerViewListAdapter(OnSearchScreenListItemClick clickListener) {
        this.clickListener = clickListener;
    }

    public void updateAndNotify(List<CategoryRecyclerViewListItem> newItems){
        items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_card,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryRecyclerViewListItem item = items.get(position);

        holder.textView.setText(item.name());
        holder.textView.setOnClickListener(v -> clickListener.onCLick(item.name()));
        holder.imageView.setOnClickListener(v -> clickListener.onCLick(item.name()));
        ImageLoader.load(holder.imageView,item.imageUrl());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            bind(itemView);
        }

        public void bind(View view){
            imageView = view.findViewById(R.id.ivIcon);
            textView = view.findViewById(R.id.tv_category_name);
        }

    }

}