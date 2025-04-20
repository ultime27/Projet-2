package com.cstre.landingpage;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import com.cstre.landingpage.databinding.ActivityMainBinding;

public class LandingPage extends AppCompatActivity {

     private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        boolean isAdmin = true;
        if (isAdmin) {
            binding.isAdminButton.setVisibility(view.VISIBLE);
        } else {
            binding.isAdminButton.setVisibility(view.INVISIBLE);
        }
        String username = "Pencil";
        if(username.equals("school")) {
            binding.usernameTextView.setVisibility(view.INVISIBLE);
        } else {
            binding.usernameTextView.setVisibility(view.VISIBLE);
            binding.usernameTextView.setText(username);
        }

        binding.isAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminPanel();
            }
        });
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                logout();
            }
        });
    }
    private void adminPanel() {
        binding.usernameTextView.setText("ADMIN");
    }

    private void logout() {
        binding.usernameTextView.setText("LOGOUT");
    }

}