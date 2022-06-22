package com.sportmogila.sporttogether;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.sportmogila.sporttogether.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    MaterialButton googleBtn;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String jsonUser = sh.getString("user", "");
        if(jsonUser.equals("")){
            setContentView(R.layout.login_activity);
        }
        else{
            openMainActivity();
        }

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if(acct!=null){
            String photo_url = "";
            if(acct.getPhotoUrl()!=null){
                photo_url = acct.getPhotoUrl().toString();
            }
            this.user = new User(acct.getDisplayName(),acct.getEmail(),photo_url,acct.getId());
        }
        googleBtn = findViewById(R.id.google_btn);
        if(googleBtn!=null){
            googleBtn.setOnClickListener(v -> {
                Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult(signInIntent,007);
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        System.out.println("LOGGED IN");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 007){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BuildConfig.API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
                if(user==null){
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

                    if(acct!=null){
                        String photo_url = "";
                        if(acct.getPhotoUrl()!=null){
                            photo_url = acct.getPhotoUrl().toString();
                        }
                        this.user = new User(acct.getDisplayName(),acct.getEmail(),photo_url,acct.getId());
                    }
                }
                Call<User> call = retrofitAPI.authUser(user);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.code()==200){
                            User user = response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            Gson gson = new Gson();
                            myEdit.putString("user", gson.toJson(user));
                            myEdit.apply();
                            openMainActivity();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Помилка сервера", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println("ERROR "+t.getMessage());
                    }
                });
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }
    void openMainActivity(){
        finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}