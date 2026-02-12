package org.wahid.foody.presentation.favorite;

import org.wahid.foody.presentation.BaseView;
import org.wahid.foody.presentation.model.MealDomainModel;

import java.util.List;

public interface FavoriteView extends BaseView {
    void updateListItems(List<MealDomainModel> items);

}
