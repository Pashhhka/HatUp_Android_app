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
import androidx.navigation.NavOptions;

import com.example.hatup.R;
import com.example.hatup.databinding.FragmentEventDetailBinding;
import com.example.hatup.viewmodel.EventDetailViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailFragment extends Fragment {

    private FragmentEventDetailBinding binding;
    private EventDetailViewModel viewModel;
    private long currentEventId = -1;
    private long currentArtistId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.homeFragment, false)
                .setLaunchSingleTop(true)
                .build();

        if (getArguments() != null) currentEventId = getArguments().getLong("eventId", -1);
        viewModel = new ViewModelProvider(this).get(EventDetailViewModel.class);

        if (currentEventId != -1) {
            viewModel.getEvent(currentEventId).observe(getViewLifecycleOwner(), event -> {
                if (event != null) {
                    currentArtistId = event.artistId;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru"));
                    String dateString = sdf.format(new Date(event.timestamp));

                    binding.tvArtistNameClickable.setText(event.title);
                    binding.tvArtistLocation.setText("📍 " + event.address + "\n🕒 " + dateString);
                    binding.tvAttendeesCount.setText(String.valueOf(event.attendeesCount));

                    viewModel.getArtist(currentArtistId).observe(getViewLifecycleOwner(), artist -> {
                        if (artist != null) {
                            binding.tvArtistNameClickable.setText("“" + artist.name + "”");
                            binding.tvArtistLocation.setText("📍 " + artist.locationName);
                        }
                    });

                    viewModel.isFavorite(currentArtistId).observe(getViewLifecycleOwner(), isFavorite -> {
                        binding.btnActionLikeEvent.setIconResource(isFavorite ?
                                android.R.drawable.btn_star_big_on : // ЗАМЕНИ НА СВОИ ИКОНКИ
                                android.R.drawable.ic_btn_speak_now);
                    });
                }
            });
        }

        binding.btnActionLikeEvent.setOnClickListener(v -> {
            if (currentArtistId != -1) {
                viewModel.toggleFavorite(currentArtistId);
            }
        });

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.tvArtistNameClickable.setOnClickListener(v -> {
            if (currentArtistId != -1) {
                Bundle bundle = new Bundle();
                bundle.putLong("artistId", currentArtistId);
                Navigation.findNavController(v).navigate(R.id.action_eventDetail_to_artistDetail, bundle);
            }
        });

        binding.btnActionAttend.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Вы записались!", Toast.LENGTH_SHORT).show();
            binding.btnActionAttend.setText("Вы идете");
            binding.btnActionAttend.setEnabled(false);
        });

        binding.btnActionLocation.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.mapFragment));
        binding.btnActionDonateEvent.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.supportFragment));
    }

    @Override
    public void onDestroyView() { super.onDestroyView(); binding = null; }
}