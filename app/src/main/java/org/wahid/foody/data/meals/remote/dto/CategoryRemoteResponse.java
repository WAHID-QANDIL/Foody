package org.wahid.foody.data.meals.remote.dto;

import java.util.List;

public record CategoryRemoteResponse (
        List<CategoryRemoteModel> categories
){}