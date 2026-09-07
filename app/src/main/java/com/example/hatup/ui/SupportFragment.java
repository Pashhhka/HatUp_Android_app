package com.example.hatup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation; // Не забываем импорт для навигации

import com.example.hatup.R;
import com.example.hatup.databinding.FragmentSupportBinding;
import com.example.hatup.model.DonationGoal;
import com.example.hatup.viewmodel.SupportViewModel;

public class SupportFragment extends Fragment {

    private FragmentSupportBinding binding;
    private SupportViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSupportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SupportViewModel.class);

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvTotalDonated.setText((int)user.totalDonatedSum + " ₽");
                binding.tvSupportedCount.setText(String.valueOf(user.visitedEventsCount));
            }
        });

        viewModel.getFavoriteArtists().observe(getViewLifecycleOwner(), artists -> {
            binding.llFavoriteArtistsContainer.removeAllViews();
            for (var artist : artists) {
                View artistView = getLayoutInflater().inflate(R.layout.item_support_artist, binding.llFavoriteArtistsContainer, false);

                ((TextView) artistView.findViewById(R.id.tv_artist_name)).setText(artist.name);

                artistView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putLong("artistId", artist.id);

                    Navigation.findNavController(requireView()).navigate(R.id.donationFragment, bundle);
                });

                LinearLayout goalsContainer = artistView.findViewById(R.id.ll_goals_in_support);
                viewModel.getGoalsByArtist(artist.id).observe(getViewLifecycleOwner(), goals -> {
                    goalsContainer.removeAllViews();
                    for (DonationGoal goal : goals) {
                        View goalView = getLayoutInflater().inflate(R.layout.item_donation_goal, goalsContainer, false);
                        ((TextView) goalView.findViewById(R.id.tv_goal_title)).setText(goal.title);
                        ((TextView) goalView.findViewById(R.id.tv_goal_current)).setText("Собрано: " + (int)goal.currentAmount + " руб");
                        ((TextView) goalView.findViewById(R.id.tv_goal_target)).setText("Цель: " + (int)goal.targetAmount + " руб");

                        int progress = (goal.targetAmount > 0) ? (int)((goal.currentAmount / goal.targetAmount) * 100) : 0;
                        ((ProgressBar) goalView.findViewById(R.id.pb_goal_progress)).setProgress(progress);
                        goalView.setClickable(false);
                        goalView.setFocusable(false);
                        if (goalView instanceof ViewGroup) {
                            ((ViewGroup) goalView).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                        }
                        goalsContainer.addView(goalView);
                    }
                });

                binding.llFavoriteArtistsContainer.addView(artistView);
            }
        });

        viewModel.getSupportedArtistsCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.tvSupportedCount.setText(String.valueOf(count));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}