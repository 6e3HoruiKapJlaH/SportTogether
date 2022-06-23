package com.sportmogila.sporttogether;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.observable.eventdata.MapLoadedEventData;
import com.mapbox.maps.plugin.delegates.listeners.OnMapLoadedListener;
import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowCreatedEventActivity extends AppCompatActivity {

    TextView eventName;
    TextView eventDescription;
    Button button;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_event_owner_activity);
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");

        eventName = findViewById(R.id.show_owner_event_name);
        eventDescription= findViewById(R.id.show_owner_event_description);
        image = findViewById(R.id.show_owner_event_image);
        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());

        MapView mapView = findViewById(R.id.show_owner_event_map);
        MapboxMap mapBoxMap = mapView.getMapboxMap();
        mapBoxMap.loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

            }
        });
        mapBoxMap.addOnMapLoadedListener(new OnMapLoadedListener() {
            @Override
            public void onMapLoaded(@NonNull MapLoadedEventData mapLoadedEventData) {
                Double x = event.getLocation().getMapX();
                Double y = event.getLocation().getMapY();
                Point point = Point.fromLngLat(y,x);
                mapBoxMap.setCamera(new CameraOptions.Builder().center(point).build());
            }
        });
        switch (event.getSport()){
            case "⚽ Футбол":
                image.setImageResource(R.drawable.sport_football);
                break;
            case "\uD83C\uDFC0 Баскетбол":
                image.setImageResource(R.drawable.sport_basketball);
                break;
            case "\uD83C\uDFD0 Волейбол":
                image.setImageResource(R.drawable.sport_volleyball);
                break;
            case "\uD83C\uDFBE Теніс":
                image.setImageResource(R.drawable.sport_tenis);
                break;
            case "\uD83E\uDD4A Бокс":
                image.setImageResource(R.drawable.sport_box);
                break;
        }
        button = findViewById(R.id.show_owner_event_button);
        button.setBackgroundColor(0xFFAF0000);

        ArrayList<User> members = event.getMembers();

        ListView membersList = findViewById(R.id.show_owner_event_members_list);
        EventMembersAdapter adapter= new EventMembersAdapter(members,getApplicationContext());
        membersList.setAdapter(adapter);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String jsonUser = sh.getString("user", "");
        User user = new Gson().fromJson(jsonUser,User.class);
        button.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
            Call<ResponseBody> call = retrofitAPI.deleteEvent(event.getId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code()==201){
                        Toast.makeText(getApplicationContext(), "Подію видалено", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Виникла помилка", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Помилка сервера", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}