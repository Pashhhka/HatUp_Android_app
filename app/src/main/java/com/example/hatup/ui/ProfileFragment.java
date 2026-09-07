package com.example.hatup.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.hatup.R;
import com.example.hatup.databinding.FragmentProfileBinding;
import com.example.hatup.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvProfileName.setText(user.nickname);
                binding.tvProfileCity.setText("📍 " + user.city);
                binding.tvStatDonations.setText((int)user.totalDonatedSum + " ₽");
            }
        });

        viewModel.getFavoriteArtists().observe(getViewLifecycleOwner(), artists -> {
            binding.llProfileFavoriteArtistsContainer.removeAllViews();

            if (artists != null) {
                binding.tvStatFavorites.setText(String.valueOf(artists.size()));

                for (var artist : artists) {
                    View artistItemView = getLayoutInflater().inflate(
                            R.layout.item_profile_favorite_artist,
                            binding.llProfileFavoriteArtistsContainer,
                            false
                    );

                    TextView tvName = artistItemView.findViewById(R.id.tv_fav_artist_name);
                    tvName.setText("“" + artist.name + "”");

                    artistItemView.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putLong("artistId", artist.id);
                        Navigation.findNavController(v).navigate(R.id.artistDetailFragment, bundle);
                    });

                    binding.llProfileFavoriteArtistsContainer.addView(artistItemView);
                }
            } else {
                binding.tvStatFavorites.setText("0");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}