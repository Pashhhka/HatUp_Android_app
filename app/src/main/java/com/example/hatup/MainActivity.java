package com.example.hatup;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.hatup.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();



        // ПРИНУДИТЕЛЬНАЯ СВЯЗЬ:
        binding.bottomNav.setOnItemSelectedListener(item -> {
            navController.navigate(item.getItemId(), null, new NavOptions.Builder()
                    .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build());
            return true;
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.eventDetailFragment ||
                    destination.getId() == R.id.artistDetailFragment||
                    destination.getId() == R.id.donationFragment||
                    destination.getId() == R.id.paymentFragment
            ) {


                binding.bottomNav.setVisibility(View.GONE);
            } else {
                // На всех остальных (Home, Map, Support) — показываем
                binding.bottomNav.setVisibility(View.VISIBLE);
            }
        });
    }

    // В MainActivity.java
    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}