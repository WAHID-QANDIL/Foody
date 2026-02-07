package org.wahid.foody.presentation;

import android.app.Activity;

import org.wahid.foody.R;
import org.wahid.foody.utils.ShowDialog;


public interface BaseView {
    void showProgressIndicator();
    void hideProgressIndicator();

    default void showErrorDialog(Activity view, String errorMessage, Runnable onOk){
        ShowDialog.show(
                view,
                R.drawable.ic_error,
                "Failed",
                errorMessage,
                R.color.divider,
                "Ok",
                onOk
        );
    }
    default void showSuccessDialog(Activity view, String message, Runnable onOk){
        ShowDialog.show(
                view,
                R.drawable.ic_success,
                "Success",
                message,
                R.color.divider,
                "Ok",
                onOk
        );

    }
}
