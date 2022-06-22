package com.sportmogila.sporttogether;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.sportmogila.sporttogether.models.Event;
import com.sportmogila.sporttogether.models.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowEventActivity extends AppCompatActivity {

    TextView eventName;
    TextView eventDescription;
    Button button;
    ButtonType btnType;
    ImageView image;

    enum ButtonType{
        MEMBER,
        NOT_MEMBER,
        OWNER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_event_activity);
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");

        eventName = findViewById(R.id.show_event_name);
        eventDescription= findViewById(R.id.show_event_description);
        image = findViewById(R.id.show_event_image);
        eventName.setText(event.getName());
        eventDescription.setText(event.getDescription());
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
        button = findViewById(R.id.show_event_button);

        ArrayList<User> members = event.getMembers();

        ListView membersList = findViewById(R.id.show_event_members_list);
        EventMembersAdapter adapter= new EventMembersAdapter(members,getApplicationContext());
        membersList.setAdapter(adapter);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String jsonUser = sh.getString("user", "");
        User user = new Gson().fromJson(jsonUser,User.class);
        if(members.contains(user)){
            modifyButton(ButtonType.MEMBER);
            btnType = ButtonType.MEMBER;
        }
        else if(event.getOwner().equals(user)){
            modifyButton(ButtonType.OWNER);
            btnType = ButtonType.OWNER;
        }
        else{
            modifyButton(ButtonType.NOT_MEMBER);
            btnType = ButtonType.NOT_MEMBER;
        }
        button.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
            Call<ResponseBody> call = null;
            if(btnType==ButtonType.NOT_MEMBER){
                call = retrofitAPI.joinEvent(event.getId(),user);
            }
            else if(btnType==ButtonType.MEMBER){
                call = retrofitAPI.leaveEvent(event.getId(),user);
            }
            if(call!=null){
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==201){
                            if(btnType==ButtonType.NOT_MEMBER){
                                modifyButton(ButtonType.MEMBER);
                                Toast.makeText(getApplicationContext(), "Ви успішно приєднались", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                modifyButton(ButtonType.NOT_MEMBER);
                                Toast.makeText(getApplicationContext(), "Ви покинули подію", Toast.LENGTH_SHORT).show();
                            }

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
            }

        });
    }

    @SuppressLint("ResourceAsColor")
    private void modifyButton(ButtonType type){
        switch(type){
            case MEMBER:
                button.setText("Покинути");
                button.setBackgroundColor(0xFFAF0000); //Red color
                break;
            case NOT_MEMBER:
                button.setEnabled(true);
                button.setText("Приєднатись");
                button.setBackgroundColor(0xFF000000); //Black color
                break;
            case OWNER:
                button.setEnabled(false);
                button.setText("Ви власник");
                break;
        }
        btnType = type;
    }
}