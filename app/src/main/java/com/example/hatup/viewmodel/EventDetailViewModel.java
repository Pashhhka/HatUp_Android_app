package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hatup.model.Artist;
import com.example.hatup.model.Event;
import com.example.hatup.repository.AppRepository;

public class EventDetailViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public EventDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<Event> getEvent(long eventId) {
        return repository.getEventById(eventId);
    }

    public LiveData<Artist> getArtist(long artistId) {
        return repository.getArtistById(artistId);
    }

    public LiveData<Boolean> isFavorite(long artistId) {
        return repository.isArtistFavorite(artistId);
    }
    public void toggleFavorite(long artistId) {
        repository.toggleFavoriteArtist(artistId);
    }
}