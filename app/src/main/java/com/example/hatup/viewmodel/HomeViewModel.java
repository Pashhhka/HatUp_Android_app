package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hatup.model.Artist;
import com.example.hatup.model.Event;
import com.example.hatup.repository.AppRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    // Три списка данных (LiveData), за которыми будет наблюдать наш Фрагмент
    private final LiveData<List<Event>> nearestEvents;
    private final LiveData<List<Event>> recommendedEvents;
    private final LiveData<List<Event>> popularEvents;

    private final AppRepository repository;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        repository = new AppRepository(application);

        nearestEvents = repository.getNearestEvents();
        recommendedEvents = repository.getRecommendedEvents();
        popularEvents = repository.getPopularEvents();
    }

    public LiveData<List<Event>> getNearestEvents() {
        return nearestEvents;
    }

    public LiveData<List<Event>> getRecommendedEvents() {
        return recommendedEvents;
    }

    public LiveData<List<Event>> getPopularEvents() {
        return popularEvents;
    }

    public void toggleFavorite(long artistId) {
        repository.toggleFavoriteArtist(artistId);
    }
    public LiveData<List<Artist>> getFavoriteArtists() {
        return repository.getFavoriteArtists();
    }
}