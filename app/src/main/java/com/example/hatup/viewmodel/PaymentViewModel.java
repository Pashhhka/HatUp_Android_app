package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.hatup.model.DonationHistory;
import com.example.hatup.repository.AppRepository;

public class PaymentViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public PaymentViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void makeDonation(DonationHistory donation) {
        repository.makeDonation(donation);
    }
}