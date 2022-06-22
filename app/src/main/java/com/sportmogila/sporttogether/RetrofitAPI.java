package com.sportmogila.sporttogether;

import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {

    //Authorize user
    @Headers({"User-Agent: sport-together-v0.1"})
    @POST("users")
    Call<User> authUser(@Body User user);

    //Get events list
    @GET("events")
    Call<ArrayList<Event>> getEvents();

    //Create new event
    @POST("events")
    Call<Event> createEvent(@Body Event event);
}
