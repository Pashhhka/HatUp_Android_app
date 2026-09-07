package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hatup.data.AppDatabase;
import com.example.hatup.model.Artist;
import com.example.hatup.model.DonationGoal;
import com.example.hatup.model.User;
import com.example.hatup.repository.AppRepository;

import java.util.List;

public class SupportViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public SupportViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<User> getCurrentUser() {
        return repository.getUserById(1);
    }

    public LiveData<List<Artist>> getFavoriteArtists() {
        return repository.getFavoriteArtists();
    }

    public LiveData<List<DonationGoal>> getGoalsByArtist(long artistId) {
        return repository.getGoalsByArtist(artistId);
    }

    public LiveData<Integer> getSupportedArtistsCount() {
        return repository.getSupportedArtistsCount();
    }
}