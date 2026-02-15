package org.wahid.foody.data.meals.remote.dto;

import com.google.gson.annotations.SerializedName;

import org.wahid.foody.domain.model.CategoryDomainModel;

public record CategoryRemoteModel(
        @SerializedName("idCategory")
        String categoryId,
        @SerializedName("strCategory")
        String categoryName,
        @SerializedName("strCategoryThumb")
        String categoryImageUrl,
        @SerializedName("strCategoryDescription")
        String categoryDescription
) {

    public CategoryDomainModel toCategoryDomainModel(){
        return new CategoryDomainModel(
          categoryId,
          categoryName,
          categoryImageUrl,
          categoryDescription
        );
    }
}