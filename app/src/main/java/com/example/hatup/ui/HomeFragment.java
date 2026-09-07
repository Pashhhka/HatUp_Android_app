package com.example.hatup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hatup.R;
import com.example.hatup.databinding.FragmentHomeBinding;
import com.example.hatup.model.Artist;
import com.example.hatup.model.Event;
import com.example.hatup.ui.adapters.EventAdapter;
import com.example.hatup.viewmodel.HomeViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private EventAdapter nearestAdapter;
    private EventAdapter recommendedAdapter;
    private EventAdapter popularAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        nearestAdapter = new EventAdapter();
        recommendedAdapter = new EventAdapter();
        popularAdapter = new EventAdapter();

        EventAdapter.OnEventClickListener eventClickListener = new EventAdapter.OnEventClickListener() {
            @Override
            public void onImageClick(Event event) {
                Bundle bundle = new Bundle();
                bundle.putLong("eventId", event.id);
                Navigation.findNavController(view).navigate(R.id.eventDetailFragment, bundle);
            }
            @Override
            public void onLocationClick(Event event) {
                Navigation.findNavController(view).navigate(R.id.mapFragment);
            }
            @Override
            public void onLikeClick(Event event, MaterialButton likeButton) {
                homeViewModel.toggleFavorite(event.artistId);
            }
        };

        nearestAdapter.setOnEventClickListener(eventClickListener);
        recommendedAdapter.setOnEventClickListener(eventClickListener);
        popularAdapter.setOnEventClickListener(eventClickListener);

        setupHorizontalRecyclerView(binding.recyclerNearest, nearestAdapter);
        setupHorizontalRecyclerView(binding.recyclerRecommended, recommendedAdapter);
        setupHorizontalRecyclerView(binding.recyclerPopular, popularAdapter);

        homeViewModel.getNearestEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null) nearestAdapter.setEvents(events);
        });
        homeViewModel.getRecommendedEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null) recommendedAdapter.setEvents(events);
        });
        homeViewModel.getPopularEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null) popularAdapter.setEvents(events);
        });

        homeViewModel.getFavoriteArtists().observe(getViewLifecycleOwner(), artists -> {
            List<Long> favIds = new ArrayList<>();
            if (artists != null) {
                for (Artist a : artists) favIds.add(a.id);
            }
            nearestAdapter.setFavoriteArtistIds(favIds);
            recommendedAdapter.setFavoriteArtistIds(favIds);
            popularAdapter.setFavoriteArtistIds(favIds);
        });
    }

    private void setupHorizontalRecyclerView(androidx.recyclerview.widget.RecyclerView recyclerView, EventAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}