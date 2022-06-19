package com.sportmogila.sporttogether.controllers;

import android.widget.Toast;

import com.sportmogila.sporttogether.BuildConfig;
import com.sportmogila.sporttogether.MainActivity;
import com.sportmogila.sporttogether.RetrofitAPI;
import com.sportmogila.sporttogether.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Authorization {

    public void authorize(User user){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<User> call = retrofitAPI.authUser(user);
        System.out.println(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("ERROR "+t.getMessage());
            }
        });
    }
}
