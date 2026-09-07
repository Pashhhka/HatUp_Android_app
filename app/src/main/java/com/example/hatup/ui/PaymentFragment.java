package com.example.hatup.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hatup.R;
import com.example.hatup.databinding.FragmentPaymentBinding;
import com.example.hatup.model.DonationHistory;
import com.example.hatup.viewmodel.PaymentViewModel;

import java.util.Locale;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding binding;
    private CountDownTimer countDownTimer;
    private int amountToPay = 0;

    private int selectedMethod = 2;

    private PaymentViewModel paymentViewModel;
    private long artistId = -1;
    private long goalId = -2;
    private String comment = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            amountToPay = getArguments().getInt("amountToPay", 0);
        }
        binding.tvPaymentAmount.setText(amountToPay + " ₽");

        startTimer();

        binding.btnBackFromPayment.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.llMethodSbp.setOnClickListener(v -> selectPaymentMethod(1));
        binding.llMethodMir.setOnClickListener(v -> selectPaymentMethod(2));
        binding.llMethodOtherCard.setOnClickListener(v -> selectPaymentMethod(3));

        if (getArguments() != null) {
            amountToPay = getArguments().getInt("amountToPay", 0);
            artistId = getArguments().getLong("artistId", -1);
            goalId = getArguments().getLong("goalId", -2);
            comment = getArguments().getString("comment", null);
        }
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        binding.btnPayNow.setOnClickListener(v -> {
            if (artistId != -1) {
                DonationHistory donation = new DonationHistory();
                donation.userId = 1;
                donation.artistId = artistId;
                donation.amount = amountToPay;
                donation.comment = comment;
                donation.timestamp = System.currentTimeMillis();
                donation.goalId = (goalId == -1 || goalId == -2) ? null : goalId;

                paymentViewModel.makeDonation(donation);
            }

            Toast.makeText(getContext(), "Оплата успешно проведена!", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).popBackStack(R.id.supportFragment, false);
        });

        selectPaymentMethod(selectedMethod);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                binding.tvPaymentTimer.setText(String.format(Locale.getDefault(), "Осталось времени: %02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                binding.tvPaymentTimer.setText("Время вышло");
            }
        }.start();
    }

    private void selectPaymentMethod(int methodIndex) {
        selectedMethod = methodIndex;

        binding.ivCheckSbp.setImageResource(android.R.drawable.checkbox_off_background);
        binding.ivCheckMir.setImageResource(android.R.drawable.checkbox_off_background);
        binding.ivCheckOther.setImageResource(android.R.drawable.checkbox_off_background);

        if (methodIndex == 1) {
            binding.ivCheckSbp.setImageResource(android.R.drawable.checkbox_on_background);
        } else if (methodIndex == 2) {
            binding.ivCheckMir.setImageResource(android.R.drawable.checkbox_on_background);
        } else if (methodIndex == 3) {
            binding.ivCheckOther.setImageResource(android.R.drawable.checkbox_on_background);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding = null;
    }
}