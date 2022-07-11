package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.widget.Button;
import android.widget.EditText;

import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity{

    private final String TAG = "MainActivityLog";
    //위험: URL 수정, 주의
    private final String URL =  "https://063a-192-249-18-215.jp.ngrok.io";


    private Retrofit retrofit;
    private com.example.myapplication.ApiService service;

    private EditText login_id, login_password;
    private Button login_button, join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = findViewById( R.id.login_id );
        login_password = findViewById( R.id.login_password );


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(com.example.myapplication.ApiService.class);
        join_button = findViewById( R.id.join_button );
        login_button = findViewById( R.id.login_button );
        join_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), Join.class );
                startActivity( intent );
            }
        });

        login_button = findViewById( R.id.login_button );
        login_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = login_id.getText().toString();
                String password = login_password.getText().toString();
                Call<Post> login = service.givelogin(id, password);
                login.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        int ID = response.body().getID();
                        Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        //위험
                        ((GlobalId)getApplication()).setGlobalId(id);
                        Intent intent = new Intent(Login.this, LobbyActivity.class);
                        startActivity(intent);
                        Log.v(TAG, "???");
                        //finish();
                    }
                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Log.v(TAG, "로그인에 실패하였습니다.");
                        Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}