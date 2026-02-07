package org.wahid.foody.presentation.details;

import android.os.Bundle;

public interface DetailsPresenter {
    void onBackClicked();
    void onShareClicked(String source);
    void onAddToFavClicked();
    void onAddToWeeklyPlay();
    void onViewCreated(Bundle bundle);

}