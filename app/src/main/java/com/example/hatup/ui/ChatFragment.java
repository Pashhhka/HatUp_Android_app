package com.example.hatup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hatup.R;
import com.example.hatup.databinding.FragmentChatBinding;
import com.example.hatup.model.Event;
import com.example.hatup.viewmodel.ChatViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;
    private ChatViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru"));

        viewModel.getNearestEvents().observe(getViewLifecycleOwner(), events -> {
            binding.llAnnouncementsContainer.removeAllViews();

            if (events != null && !events.isEmpty()) {
                int limit = Math.min(events.size(), 2);

                for (int i = 0; i < limit; i++) {
                    Event event = events.get(i);
                    View announcementView = getLayoutInflater().inflate(R.layout.item_chat_announcement, binding.llAnnouncementsContainer, false);

                    TextView tvLocation = announcementView.findViewById(R.id.tv_announcement_location);
                    TextView tvDate = announcementView.findViewById(R.id.tv_announcement_date);
                    Button btnEventCard = announcementView.findViewById(R.id.btn_event_card);
                    Button btnLocation = announcementView.findViewById(R.id.btn_location);

                    tvLocation.setText(event.address);
                    tvDate.setText(sdf.format(new Date(event.timestamp)));

                    TextView tvArtistName = announcementView.findViewById(R.id.tv_announcement_artist);
                    viewModel.getArtistById(event.artistId).observe(getViewLifecycleOwner(), artist -> {
                        if (artist != null) {
                            tvArtistName.setText(artist.name);
                        }
                    });

                    btnEventCard.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putLong("eventId", event.id);
                        Navigation.findNavController(v).navigate(R.id.eventDetailFragment, bundle);
                    });

                    btnLocation.setOnClickListener(v -> {
                        Navigation.findNavController(v).navigate(R.id.mapFragment);
                    });

                    binding.llAnnouncementsContainer.addView(announcementView);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}