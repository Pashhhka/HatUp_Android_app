package com.example.hatup.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hatup.R;
import com.example.hatup.databinding.FragmentArtistDetailBinding;
import com.example.hatup.model.DonationGoal;
import com.example.hatup.viewmodel.ArtistDetailViewModel;

public class ArtistDetailFragment extends Fragment {

    private FragmentArtistDetailBinding binding;
    private ArtistDetailViewModel viewModel;
    private long currentArtistId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) currentArtistId = getArguments().getLong("artistId", -1);
        viewModel = new ViewModelProvider(this).get(ArtistDetailViewModel.class);
        if (currentArtistId != -1) {
            viewModel.isFavorite(currentArtistId).observe(getViewLifecycleOwner(), isFavorite -> {
                android.util.Log.d("DEBUG_UI", "Is favorite: " + isFavorite);

                int iconRes = isFavorite ? android.R.drawable.btn_star_big_on : android.R.drawable.ic_btn_speak_now;

                binding.btnActionLikeArtist.setIcon(ContextCompat.getDrawable(requireContext(), iconRes));

                binding.btnActionLikeArtist.invalidate();
                binding.btnActionLikeArtist.requestLayout();
            });
        }

        binding.btnActionLikeArtist.setOnClickListener(v -> {
            if (currentArtistId != -1) {
                viewModel.toggleFavorite(currentArtistId);
            }
        });

        if (currentArtistId != -1) {
            viewModel.getArtist(currentArtistId).observe(getViewLifecycleOwner(), artist -> {
                if (artist != null) binding.tvArtistNameTitle.setText("“" + artist.name + "”");
            });

            viewModel.getGoals(currentArtistId).observe(getViewLifecycleOwner(), goals -> {
                binding.llDonationGoalsContainer.removeAllViews();
                if (goals != null) {
                    for (DonationGoal goal : goals) {
                        View v = getLayoutInflater().inflate(R.layout.item_donation_goal, binding.llDonationGoalsContainer, false);
                        ((TextView) v.findViewById(R.id.tv_goal_title)).setText(goal.title);
                        ((TextView) v.findViewById(R.id.tv_goal_current)).setText("Собрано: " + (int)goal.currentAmount + " руб");
                        ((TextView) v.findViewById(R.id.tv_goal_target)).setText("Цель: " + (int)goal.targetAmount + " руб");

                        int progress = (goal.targetAmount > 0) ? (int)((goal.currentAmount / goal.targetAmount) * 100) : 0;
                        ((ProgressBar) v.findViewById(R.id.pb_goal_progress)).setProgress(progress);

                        v.setOnClickListener(click -> Navigation.findNavController(v).navigate(R.id.supportFragment));
                        binding.llDonationGoalsContainer.addView(v);
                    }
                }
            });
        }

        binding.btnBackArtist.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.btnActionMessage.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.chatFragment));
        binding.btnActionSupportMain.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.supportFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}