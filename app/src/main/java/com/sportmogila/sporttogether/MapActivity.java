package com.sportmogila.sporttogether;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.mapbox.geojson.Point;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.sportmogila.sporttogether.models.Location;
import com.sportmogila.sporttogether.models.User;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        MapView mapView = findViewById(R.id.add_event_map);
        MapboxMap mapBoxMap = mapView.getMapboxMap();
        mapBoxMap.loadStyleUri(Style.MAPBOX_STREETS);
        Button mapChoose = findViewById(R.id.add_event_map_choose);
        mapChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Point point = mapView.getMapboxMap().getCameraState().getCenter();
                Intent data = new Intent();
                Location lastLocation = (Location) data.getSerializableExtra("location");
                List<Double> cds = point.coordinates();

                Geocoder gc = new Geocoder(MapActivity.this);
                List<Address> list = null;

                if(Geocoder.isPresent()) {
                    try {
                        list = gc.getFromLocation(cds.get(1), cds.get(0), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (!list.isEmpty()) {
                        Address address = list.get(0);
                        // CAN BE NULL CITY, NEED TO PREVENT
                        Location location = new Location(address.getCountryName(),address.getLocality(),cds.get(1),cds.get(0));
                        Intent intent = new Intent();
                        intent.putExtra("location",location);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }
}