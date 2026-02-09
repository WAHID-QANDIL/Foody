package org.wahid.foody.presentation.search.countries_list_adapter;

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

import java.util.Collections;
import java.util.List;

public class CountriesRecyclerViewAdapter extends RecyclerView.Adapter<CountriesRecyclerViewAdapter.CountryViewHolder> {


    private List<CountryListItem> items = Collections.emptyList();
    private static final String TAG = "CountriesRecyclerViewAd";

    public void updateAndNotify(List<CountryListItem> newItems){
        items = newItems;
        Log.d(TAG, "updateAndNotify: " + newItems);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_row,parent,false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryListItem countryListItem = items.get(position);
        holder.textView.setText(countryListItem.name());
        ImageLoader.load(holder.imageView, countryListItem.flagImageUrl());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CountryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            bind(itemView);
        }
        public void bind(View view){
            imageView = view.findViewById(R.id.ivIcon);
            textView = view.findViewById(R.id.tv_country_name);
        }
    }
}