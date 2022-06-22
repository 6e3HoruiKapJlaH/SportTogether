package com.sportmogila.sporttogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
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
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    User user;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    SwipeRefreshLayout swipeRefreshLayout;

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
            Fragment selectedFragment = null;

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
                case R.id.menu_account:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_all_events).setChecked(true);
                        showEvents();
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_my_events).setChecked(true);
                        break;
                    case 2:
                        createEventPage();
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_account).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //CREATE EVENT PAGE
    public void createEventPage(){

        //APPEND SPORT TYPES TO THE SELECT SPORT INPUT
        String[] sportArray = getResources().getStringArray(R.array.sport_types);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.dropdown_sport_type, sportArray);
        AutoCompleteTextView actView = findViewById(R.id.add_event_sport_list);
        actView.setAdapter(arrayAdapter);
        bottomNavigationView.getMenu().findItem(R.id.menu_add_event).setChecked(true);

        //CLICK CREATE BUTTON
        Button createEventButton = findViewById(R.id.add_event_button);
        createEventButton.setOnClickListener(v -> {
            EditText name = findViewById(R.id.add_event_name);
            EditText description = findViewById(R.id.add_event_description);
            TextInputLayout sport = findViewById(R.id.add_event_sport);
            String event_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            Location location = new Location("Україна","Київ",41.40338,2.17403);
            Event event = new Event(name.getText().toString(),description.getText().toString(),sport.getEditText().getText().toString(),
                    user,location,event_at);
            if(validateCreateEventForm(event)){
                createEvent(event);
            }
        });
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
                    System.out.println(events);
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

}