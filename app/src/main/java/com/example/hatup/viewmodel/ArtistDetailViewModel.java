package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hatup.model.Artist;
import com.example.hatup.model.DonationGoal; // Не забудь этот импорт!
import com.example.hatup.repository.AppRepository;

import java.util.List;

public class ArtistDetailViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public ArtistDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<Artist> getArtist(long artistId) {
        return repository.getArtistById(artistId);
    }

    public LiveData<List<DonationGoal>> getGoals(long artistId) {
        return repository.getGoalsByArtist(artistId);
    }

    public LiveData<Boolean> isFavorite(long artistId) {
        return repository.isArtistFavorite(artistId);
    }

    public void toggleFavorite(long artistId) {
        repository.toggleFavoriteArtist(artistId);
    }
}