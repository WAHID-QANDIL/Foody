package org.wahid.foody.domain.model;

public record CategoryDomainModel(
        String categoryId,
        String categoryName,
        String categoryImageUrl,
        String categoryDescription
) { }