package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hatup.model.Artist;
import com.example.hatup.model.DonationGoal;
import com.example.hatup.repository.AppRepository;

import java.util.List;

public class DonationViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public DonationViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<Artist> getArtist(long artistId) {
        return repository.getArtistById(artistId);
    }

    public LiveData<List<DonationGoal>> getGoalsByArtist(long artistId) {
        return repository.getGoalsByArtist(artistId);
    }
}