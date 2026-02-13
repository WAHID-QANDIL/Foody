package org.wahid.foody.presentation.navigation;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.navigation.Navigation;

public abstract class AppNavigator {

    public static void navigate(View view, @IdRes int resourceId){
        Navigation.findNavController(view).navigate(resourceId);
    }

}
