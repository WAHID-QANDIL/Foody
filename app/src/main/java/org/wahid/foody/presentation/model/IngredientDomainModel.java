package org.wahid.foody.presentation.model;

public record IngredientDomainModel(
        String id,
        String name,
        String description,
        String imageUrl,
        String type
) {
}
