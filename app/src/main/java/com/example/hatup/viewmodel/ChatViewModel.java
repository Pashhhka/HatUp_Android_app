package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hatup.model.Artist;
import com.example.hatup.model.Event;
import com.example.hatup.repository.AppRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<List<Event>> getNearestEvents() {
        return repository.getNearestEvents();
    }

    public LiveData<Artist> getArtistById(long artistId) {
        return repository.getArtistById(artistId);
    }
}