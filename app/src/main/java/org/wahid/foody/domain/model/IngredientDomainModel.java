package org.wahid.foody.domain.model;

public record IngredientDomainModel(
        String id,
        String name,
        String description,
        String imageUrl,
        String type
) {
}
