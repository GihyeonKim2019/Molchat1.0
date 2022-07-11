package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Join extends AppCompatActivity{
    private final String TAG = "MainActivityLog";
    // 주의
    private final String URL =  "https://063a-192-249-18-215.jp.ngrok.io";


    private Retrofit retrofit;
    ApiService service;

    private EditText join_id, join_password, join_name, join_pwck;
    private Button join_button, check_button;
    private AlertDialog dialog;
    private boolean validate = false;
    private boolean finish = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_id = findViewById(R.id.join_id);
        join_password = findViewById(R.id.join_password);
        join_name = findViewById(R.id.join_name);
        join_pwck = findViewById(R.id.join_pwck);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);

        check_button = findViewById(R.id.check_button);
        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = join_id.getText().toString();
                Call<Post> login0 = service.giveid(id);
                login0.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {

                        String userid = response.body().getUserid();
                        if (id.equals(userid)) {
                            Log.v(TAG, "회원 가입-3");
                            Log.v(TAG, "아이디가 중복됩니다.");
                            Toast.makeText(getApplicationContext(), "아이디가 중복됩니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        validate = true;
                        Log.v(TAG, "아이디가 중복되지 않습니다.");
                        Toast.makeText(getApplicationContext(), "아이디가 중복되지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Log.v(TAG, "회원 가입0");
        join_button = findViewById(R.id.join_button);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = join_name.getText().toString();
                String id = join_id.getText().toString();
                String password = join_password.getText().toString();
                String pwck = join_pwck.getText().toString();
                Log.v(TAG, "회원 가입1");
                if(validate && password.equals(pwck)){
                    //로그인 db에 데이터 넣기
                    //login 화면으로 이동
                    Log.v(TAG, "회원 가입2");
                    Call<Post> login1 = service.giveall(name, id, password);
                    Log.v(TAG, "회원 가입3");
                    login1.enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            finish = true;
                            Log.v(TAG, "회원 가입에 성공하였습니다.");
                            Toast.makeText(getApplicationContext(), "회원 가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            Log.v(TAG, "서버 접속에 실패하였습니다.");
                            Toast.makeText(getApplicationContext(), "서버 접속에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
