package com.sportmogila.sporttogether;

import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @Headers({"User-Agent: sport-together-v0.1"})
    @POST("users")
    Call<User> authUser(@Body User user);

    @Headers({"User-Agent: sport-together-v0.1"})
    @GET("events")
    Call<Event> getEvents(@Body User user);
}
