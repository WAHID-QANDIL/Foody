package org.wahid.foody.ui;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wahid.foody.R;
import org.wahid.foody.databinding.FragmentFirstOnboardingScreenBinding;

import java.util.Objects;

public class FirstOnboardingScreenFragment extends Fragment {


    FragmentFirstOnboardingScreenBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar toolbar = Objects.requireNonNull(requireActivity()).getActionBar();
        if (toolbar!= null){

            toolbar.hide();
        }
        binding = FragmentFirstOnboardingScreenBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.firstOnboardingImage.setImageResource(R.drawable.fried_chicken_with_vegetables_herbs_aluminum_skillet);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_from_onboarding1_to_onboarding2);
            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}