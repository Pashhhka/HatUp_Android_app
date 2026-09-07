package com.example.hatup.ui;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.hatup.databinding.FragmentArtistDonationBinding;
import com.example.hatup.model.DonationGoal;
import com.example.hatup.viewmodel.DonationViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DonationFragment extends Fragment {

    private FragmentArtistDonationBinding binding;
    private DonationViewModel viewModel;

    private long currentArtistId = -1;

    private long selectedGoalId = -2;
    private int currentAmount = 0;
    private boolean isUpdatingAmountProgrammatically = false; // Флаг для TextWatcher

    private List<View> goalCards = new ArrayList<>();
    private MaterialButton[] amountButtons;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtistDonationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            currentArtistId = getArguments().getLong("artistId", -1);
        }

        viewModel = new ViewModelProvider(this).get(DonationViewModel.class);

        amountButtons = new MaterialButton[]{
                binding.btnAmount100,
                binding.btnAmount300,
                binding.btnAmount500,
                binding.btnAmount1000
        };

        if (currentArtistId != -1) {
            viewModel.getArtist(currentArtistId).observe(getViewLifecycleOwner(), artist -> {
                if (artist != null) {
                    binding.tvDonationArtistName.setText(artist.name);
                }
            });

            viewModel.getGoalsByArtist(currentArtistId).observe(getViewLifecycleOwner(), goals -> {
                binding.llDonationGoalsSelectable.removeAllViews();
                goalCards.clear();

                if (goals != null) {
                    for (DonationGoal goal : goals) {
                        addGoalCard(goal.id, goal.title, "Поддержка сбора средств", goal.currentAmount, goal.targetAmount);
                    }
                }
                addGoalCard(-1, "Основной донат", "Поддерживайте артиста в любых начинаниях", 0, 0);
            });
        }

        binding.btnAmount100.setOnClickListener(v -> updateAmountSelection(binding.btnAmount100, 100));
        binding.btnAmount300.setOnClickListener(v -> updateAmountSelection(binding.btnAmount300, 300));
        binding.btnAmount500.setOnClickListener(v -> updateAmountSelection(binding.btnAmount500, 500));
        binding.btnAmount1000.setOnClickListener(v -> updateAmountSelection(binding.btnAmount1000, 1000));

        binding.etCustomAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingAmountProgrammatically) return;

                if (s.length() > 0) {
                    resetAmountButtons();
                    try {
                        currentAmount = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        currentAmount = 0;
                    }
                } else {
                    currentAmount = 0;
                }
                checkFormValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.btnBackToSupport.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.btnSubmitDonation.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("amountToPay", currentAmount);
            bundle.putLong("artistId", currentArtistId);
            bundle.putLong("goalId", selectedGoalId);

            String comment = binding.etDonationComment.getText().toString().trim();
            if (currentAmount >= 500 && !comment.isEmpty()) {
                bundle.putString("comment", comment);
            }

            Navigation.findNavController(v).navigate(R.id.paymentFragment, bundle);
        });

        checkFormValidity();
    }

    private void addGoalCard(long goalId, String title, String desc, double current, double target) {
        View goalView = getLayoutInflater().inflate(R.layout.item_donation_goal_selectable, binding.llDonationGoalsSelectable, false);

        MaterialCardView cardContainer = goalView.findViewById(R.id.card_goal_container);
        ImageView checkIcon = goalView.findViewById(R.id.iv_goal_check);
        LinearLayout progressBlock = goalView.findViewById(R.id.ll_goal_progress_block);

        ((TextView) goalView.findViewById(R.id.tv_goal_title)).setText(title);
        ((TextView) goalView.findViewById(R.id.tv_goal_desc)).setText(desc);

        if (goalId == -1) {
            progressBlock.setVisibility(View.GONE);
        } else {
            progressBlock.setVisibility(View.VISIBLE);
            ((TextView) goalView.findViewById(R.id.tv_goal_current)).setText("Собрано: " + (int)current + " руб");
            ((TextView) goalView.findViewById(R.id.tv_goal_target)).setText("Цель: " + (int)target + " руб");
            int progress = (target > 0) ? (int)((current / target) * 100) : 0;
            ((ProgressBar) goalView.findViewById(R.id.pb_goal_progress)).setProgress(progress);
        }

        cardContainer.setTag(goalId);
        goalCards.add(cardContainer);

        cardContainer.setOnClickListener(v -> {
            selectedGoalId = (long) v.getTag();

            for (View card : goalCards) {
                MaterialCardView materialCard = (MaterialCardView) card;
                ImageView icon = card.findViewById(R.id.iv_goal_check);

                if ((long) card.getTag() == selectedGoalId) {
                    materialCard.setStrokeWidth(3);
                    icon.setVisibility(View.VISIBLE);
                } else {
                    materialCard.setStrokeWidth(0);
                    icon.setVisibility(View.GONE);
                }
            }
            checkFormValidity();
        });

        binding.llDonationGoalsSelectable.addView(goalView);
    }

    private void updateAmountSelection(MaterialButton selectedBtn, int amount) {
        currentAmount = amount;

        isUpdatingAmountProgrammatically = true;
        binding.etCustomAmount.setText("");
        isUpdatingAmountProgrammatically = false;

        for (MaterialButton btn : amountButtons) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT)); // Прозрачный фон
            btn.setTextColor(Color.parseColor("#FFDB93")); // Желтый текст
        }

        selectedBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFDB93")));
        selectedBtn.setTextColor(Color.parseColor("#191919"));

        checkFormValidity();
    }

    private void resetAmountButtons() {
        for (MaterialButton btn : amountButtons) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btn.setTextColor(Color.parseColor("#FFDB93"));
        }
    }

    private void checkFormValidity() {
        if (currentAmount >= 500) {
            binding.etDonationComment.setEnabled(true);
            binding.etDonationComment.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            binding.etDonationComment.setHint("Напиши теплые слова артисту...");
        } else {
            binding.etDonationComment.setEnabled(false);
            binding.etDonationComment.setText("");
            binding.etDonationComment.setHint("Опиши цель сбора подробнее");
        }

        boolean isGoalSelected = selectedGoalId != -2;
        boolean isAmountValid = currentAmount > 0;

        binding.btnSubmitDonation.setEnabled(isGoalSelected && isAmountValid);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}