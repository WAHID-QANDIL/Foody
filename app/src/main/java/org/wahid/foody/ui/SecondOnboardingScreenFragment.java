package org.wahid.foody.ui;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentFirstOnboardingScreenBinding;
import org.wahid.foody.databinding.FragmentSecondOnboardingScreenBinding;


public class SecondOnboardingScreenFragment extends Fragment {


    FragmentSecondOnboardingScreenBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSecondOnboardingScreenBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.secondOnboardingImage.setImageResource(R.drawable.dishes_brown_surface);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}