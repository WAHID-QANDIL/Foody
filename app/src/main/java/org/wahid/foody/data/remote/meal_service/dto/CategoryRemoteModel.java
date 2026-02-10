package org.wahid.foody.data.remote.meal_service.dto;

import com.google.gson.annotations.SerializedName;

import org.wahid.foody.presentation.model.CategoryDomainModel;

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