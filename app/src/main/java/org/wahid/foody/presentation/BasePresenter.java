package org.wahid.foody.presentation;

import android.app.Activity;


public interface BasePresenter {
    void showProgressIndicator();
    void hideProgressIndicator();
    void showErrorDialog(Activity view, String errorMessage, Runnable onOk);
    void showSuccessDialog(Activity view, String message, Runnable onOk);
}
