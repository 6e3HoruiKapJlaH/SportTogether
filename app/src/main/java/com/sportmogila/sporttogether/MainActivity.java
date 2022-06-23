package com.sportmogila.sporttogether;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SublimeDatePicker;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData;
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener;
import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.Location;
import com.sportmogila.sporttogether.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    User user;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;

    Location location;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        showEvents();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String jsonUser = sh.getString("user", "");
        System.out.println(jsonUser);
        user = new Gson().fromJson(jsonUser,User.class);

        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_all_events:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.menu_my_events:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.menu_add_event:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.menu_created_events:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.menu_account:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_all_events).setChecked(true);
                        showEvents();
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_my_events).setChecked(true);
                        showMyEvents();
                        break;
                    case 2:
                        createEventPage();
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_created_events).setChecked(true);
                        showCreatedEvents();
                        break;
                    case 4:
                        showAccountPage();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //CREATE EVENT PAGE
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createEventPage(){

        //APPEND SPORT TYPES TO THE SELECT SPORT INPUT
        String[] sportArray = getResources().getStringArray(R.array.sport_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.dropdown_sport_type, sportArray);
        AutoCompleteTextView actView = findViewById(R.id.add_event_sport_list);
        actView.setAdapter(arrayAdapter);
        bottomNavigationView.getMenu().findItem(R.id.menu_add_event).setChecked(true);



        //MAP
        Button mapButton = findViewById(R.id.add_event_open_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("location",location);
                startActivityForResult(intent,777);
            }
        });
        //CLICK CREATE BUTTON
        Button createEventButton = findViewById(R.id.add_event_button);
        createEventButton.setOnClickListener(v -> {
            EditText name = findViewById(R.id.add_event_name);
            EditText description = findViewById(R.id.add_event_description);
            TextInputLayout sport = findViewById(R.id.add_event_sport);
            String event_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            Event event = new Event(name.getText().toString(),description.getText().toString(),sport.getEditText().getText().toString(),
                    user,location,event_at);
            if(validateCreateEventForm(event)){
                createEvent(event);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==777){
            if(resultCode==RESULT_OK){
                this.location = (Location) data.getSerializableExtra("location");
            }
        }
    }

    public boolean validateCreateEventForm(Event event){
        if(event.getName().equals("")){
            return false;
        }
        if(event.getDescription().equals("")){
            return false;
        }
        if(event.getSport().equals("")){
            return false;
        }
        System.out.println("VALIDATE "+location);
        if(event.getLocation()==null){
            return false;
        }
        return true;
    }

    public void showEvents(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ArrayList<Event>> call = retrofitAPI.getEvents();
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Event>> call, @NonNull Response<ArrayList<Event>> response) {
                if(response.code()==200){
                    ArrayList<Event> events = response.body();
                    ListView allEventsList = (ListView) findViewById(R.id.all_events_list);
                    AllEventsAdapter adapter= new AllEventsAdapter(events,getApplicationContext());
                    allEventsList.setAdapter(adapter);
                    allEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Event event = events.get(i);
                            Intent intent = new Intent(MainActivity.this, ShowEventActivity.class);
                            intent.putExtra("event", event);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Помилка завантаження подій", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Event>> call, @NonNull Throwable t) {
                System.out.println("ERROR "+t.getMessage());
            }
        });
    }

    public void showMyEvents(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ArrayList<Event>> call = retrofitAPI.getMyEvents(user);
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Event>> call, @NonNull Response<ArrayList<Event>> response) {
                if(response.code()==200){
                    ArrayList<Event> events = response.body();
                    ListView allEventsList = (ListView) findViewById(R.id.my_events_list);
                    AllEventsAdapter adapter= new AllEventsAdapter(events,getApplicationContext());
                    allEventsList.setAdapter(adapter);
                    allEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Event event = events.get(i);
                            Intent intent = new Intent(MainActivity.this, ShowEventActivity.class);
                            intent.putExtra("event", event);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Помилка завантаження подій", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Event>> call, @NonNull Throwable t) {
                System.out.println("ERROR "+t.getMessage());
            }
        });
    }

    public void showCreatedEvents(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ArrayList<Event>> call = retrofitAPI.getCreatedEvents(user);
        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Event>> call, @NonNull Response<ArrayList<Event>> response) {
                if(response.code()==200){
                    ArrayList<Event> events = response.body();
                    ListView allEventsList = (ListView) findViewById(R.id.created_events_list);
                    AllEventsAdapter adapter= new AllEventsAdapter(events,getApplicationContext());
                    allEventsList.setAdapter(adapter);
                    allEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Event event = events.get(i);
                            Intent intent = new Intent(MainActivity.this, ShowCreatedEventActivity.class);
                            intent.putExtra("event", event);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Помилка завантаження подій", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Event>> call, @NonNull Throwable t) {
                System.out.println("ERROR "+t.getMessage());
            }
        });
    }

    private void showAccountPage(){
        bottomNavigationView.getMenu().findItem(R.id.menu_account).setChecked(true);
        TextView username = findViewById(R.id.account_name);
        TextView email = findViewById(R.id.account_email);
        Button signout = findViewById(R.id.signout);
        username.setText(user.getName());
        email.setText(user.getEmail());

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sh.edit();
                editor.clear().apply();
                finish();
            }
        });
    }

    public void createEvent(Event event){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<Event> call = retrofitAPI.createEvent(event);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if(response.code()==200){
                    Event event = response.body();
                    Toast.makeText(getApplicationContext(), "Подію успішно створено", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(1);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Помилка сервера", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                System.out.println("ERROR "+t.getMessage());
            }
        });
    }

    //FILTERS

    private void filterByFootball() {
        Toast.makeText(this, "Filtered by football ⚽️", Toast.LENGTH_SHORT).show();
    }
    private void filterByBasketball() {
        Toast.makeText(this, "Filtered by basketball 🏀", Toast.LENGTH_SHORT).show();

    }
    private void sortByDate() {
        Toast.makeText(this, "Sorted by date 📅", Toast.LENGTH_SHORT).show();

    }
    private void sortByName() {
        Toast.makeText(this, "Sorted by name 🎫", Toast.LENGTH_SHORT).show();
    }
}