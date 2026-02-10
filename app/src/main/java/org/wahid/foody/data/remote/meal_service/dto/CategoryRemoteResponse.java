package org.wahid.foody.data.remote.meal_service.dto;

import java.util.List;

public record CategoryRemoteResponse (
        List<CategoryRemoteModel> categories
){}