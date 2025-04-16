package com.suchet.smartFridge.database.entities;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.suchet.smartFridge.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SmartFridgeRepository {
    private UserDAO userDAO;
    private static SmartFridgeRepository repository;
    private  SmartFridgeRepository(Application application){
        SmartFridgeDatabase db= SmartFridgeDatabase.getDatabase(application);
        this.userDAO = db.userDAO();
    }

    public static SmartFridgeRepository getRepository(Application application){
        if (repository!=null){
            return repository;
        }
        Future<SmartFridgeRepository> future = SmartFridgeDatabase.dataBaseWriteExecutor.submit(
                new Callable<SmartFridgeRepository>() {
                    @Override
                    public SmartFridgeRepository call() throws Exception {
                        repository= new SmartFridgeRepository(application);
                        return repository;
                    }
                }
        );
        try{
            return future.get();
        } catch (InterruptedException | ExecutionException e){
            Log.d(MainActivity.TAG,"Problem getting GymLogRepository, thread error.");
        }
        return null;
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

}
