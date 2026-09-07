package com.example.hatup.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.hatup.model.Artist;
import com.example.hatup.model.User;
import com.example.hatup.repository.AppRepository;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private final AppRepository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<User> getCurrentUser() {
        return repository.getCurrentUser();
    }

    public LiveData<List<Artist>> getFavoriteArtists() {
        return repository.getFavoriteArtists();
    }
}