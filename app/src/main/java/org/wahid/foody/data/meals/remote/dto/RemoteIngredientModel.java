package org.wahid.foody.data.meals.remote.dto;

import com.google.gson.annotations.SerializedName;

import org.wahid.foody.domain.model.IngredientDomainModel;

public record RemoteIngredientModel(
        @SerializedName("idIngredient")
        String id,
        @SerializedName("strIngredient")
        String name,
        @SerializedName("strDescription")
        String description,
        @SerializedName("strThumb")
        String imageUrl,
        @SerializedName("strType")
        String type
) {



    public IngredientDomainModel toIngredientDomainModel(){
        return new IngredientDomainModel(id,name,description,imageUrl,type == null? "Unknown" : type);
    }
}
