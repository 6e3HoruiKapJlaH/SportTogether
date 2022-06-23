package com.sportmogila.sporttogether;

import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {

    //Authorize user
    @Headers({"User-Agent: sport-together-v0.1"})
    @POST("users")
    Call<User> authUser(@Body User user);

    //Get events list
    @GET("events")
    Call<ArrayList<Event>> getEvents();

    //Get my events list
    @POST("events/member")
    Call<ArrayList<Event>> getMyEvents(@Body User user);

    //Get created events list
    @POST("events/owner")
    Call<ArrayList<Event>> getCreatedEvents(@Body User user);

    //Create new event
    @POST("events")
    Call<Event> createEvent(@Body Event event);

    //Join to the event
    @POST("events/{id}/join")
    Call<ResponseBody> joinEvent(
            @Path("id") int id,
            @Body User user
    );

    //Leave the event
    @POST("events/{id}/leave")
    Call<ResponseBody> leaveEvent(
            @Path("id") int id,
            @Body User user
    );

    //Delete the event
    @DELETE("events/{id}")
    Call<ResponseBody> deleteEvent(
            @Path("id") int id
    );
}
