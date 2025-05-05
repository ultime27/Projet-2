package com.suchet.smartFridge;

import static com.suchet.smartFridge.MainActivity.TAG;
import static com.suchet.smartFridge.Recipie.SuggestionPageActivity.suggestionPageActivityIntentFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.suchet.smartFridge.database.SmartFridgeDatabase;
import com.suchet.smartFridge.database.SmartFridgeRepository;
import com.suchet.smartFridge.database.UserDAO;
import com.suchet.smartFridge.database.entities.User;
import com.suchet.smartFridge.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private SmartFridgeRepository repository;
    private SmartFridgeDatabase db;
    private UserDAO userDAO= db.userDAO();

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = SmartFridgeRepository.getRepository(getApplication());

        signInButton();
        logoutButton();
    }

    private void signInButton() {
        binding.SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyUser(); }
        });
    }

    private void loginButton() {
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginActivity();
            }
        });
    }

    private void logoutButton(){
        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.mainActivityIntentFactory(getApplicationContext(),-1));
            }
        });
    }

    private void loginActivity() {
        startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
    }


    private void verifyUser() {
        String username = binding.userNameRegisterEditText.getText().toString();
        String password = binding.passwordRegisterEditText.getText().toString();
        String confirmPassword = binding.ConfirmPasswordRegisterEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("Username may not be blank");
            return;
        }
        if(password.isEmpty() || confirmPassword.isEmpty()) {
            toastMaker("Password may not be blank");
            return;
        }

        if(password.equals(confirmPassword)){
            repository.insert(username, password);
            LiveData<User> userObserver = repository.getUserByUsername(username);
            userObserver.observe(this, user -> {
                startActivity(LandingPage.landingPageActivityIntentFactory(getApplicationContext(),user.getId()));
            });
        } else {
            Log.d(TAG, "Passwords do not match.");
            binding.passwordRegisterEditText.setSelection(0);
        }
    }

    private void toastMaker(String format) {
        Toast.makeText(this, format, Toast.LENGTH_SHORT).show();
    }

    static Intent RegisterIntentFactory(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
}