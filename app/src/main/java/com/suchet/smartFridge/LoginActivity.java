package com.suchet.smartFridge;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suchet.smartFridge.database.SmartFridgeRepository;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityLoginBinding;
import com.suchet.smartFridge.stocks.StockActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SmartFridgeRepository repository;
    private GoogleSignInActivity googleSignInActivity;

    //    private GoogleSignInActivity googleSignInActivity;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = SmartFridgeRepository.getRepository(getApplication());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            toastMaker("Firebase logged in.");
            //reload();
        } else {
            toastMaker("Firebase not logged in.");
        }

        SignInButton();
        CreateAccountButton();
        logoutButton();
    }

    private void CreateAccountButton() {
        binding.CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerActivity();
            }
        });

    }

    private void SignInButton() {
        binding.SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });
    }

    private void registerActivity() {
        startActivity(RegisterActivity.RegisterIntentFactory(getApplicationContext()));
    }

    private void verifyUser() {

        String username = binding.userNameLoginEditText.getText().toString();
        if(username.isEmpty()){
            toastMaker("Username may not be blank");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUsername(username);
        userObserver.observe(this, user -> {
            if(user != null){
                String password = binding.passwordLoginEditText.getText().toString();
                if(password.equals(user.getPassword())){
                    startActivity(LandingPage.landingPageActivityIntentFactory(getApplicationContext(),user.getId()));
                }else {
                    toastMaker("Invalid password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            }else {
                toastMaker(String.format("%s is not a valid username", username));
                binding.userNameLoginEditText.setSelection(0);
            }
        });
    }

    private void toastMaker(String format) {
        Toast.makeText(this, format, Toast.LENGTH_SHORT).show();
    }

    public static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
    private void logoutButton(){
        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),-1));
            }
        });
    }
}