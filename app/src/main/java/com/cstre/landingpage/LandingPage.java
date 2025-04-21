package com.cstre.landingpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.view.View;
import com.cstre.landingpage.databinding.ActivityMainBinding;

import com.suchet.smartFridge.LoginActivity;
import com.suchet.smartFridge.MainActivity;
import com.suchet.smartFridge.database.SmartFridgeRepository;
import com.suchet.smartFridge.database.entities.User;

public class LandingPage extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SmartFridgeRepository repository;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

	repository = SmartFridgeRepository.getRepository(getApplication());
	LiveData<User> userObserver = repository.getUserByUsername(username);

        binding.isAdminButton.setVisibility(view.INVISIBLE);
        userObserver.observe(this, user -> {
            if(user.isAdmin()){
                binding.isAdminButton.setVisibility(view.VISIBLE);
            }
        });

        binding.isAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminPanel();
            }
        });
        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
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
        //startActivity(MainActivity.MainIntentFactory(getApplicationContext()));
    }

    static Intent LandingPageIntentFactory(Context context) {
        return new Intent(context, LandingPage.class);
    }

}