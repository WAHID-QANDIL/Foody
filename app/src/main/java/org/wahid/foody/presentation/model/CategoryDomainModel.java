package org.wahid.foody.presentation.model;

public record CategoryDomainModel(
        String categoryId,
        String categoryName,
        String categoryImageUrl,
        String categoryDescription
) { }